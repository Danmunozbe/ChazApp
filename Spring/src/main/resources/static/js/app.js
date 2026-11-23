/*
 * ChazApp - una sola pantalla, dos modos (comprador / vendedor).
 *
 *  Comprador (CU-05, CU-01):
 *    - Lista chazas y sus menús por categoría.
 *    - Carrito que mezcla productos de varias chazas; al confirmar se crea un
 *      pedido por chaza (POST /api/compras) y se muestra la confirmación inline.
 *    - CU-05-M01: refresca la disponibilidad periódicamente; agotados se ven distinto.
 *    - CU-01-M01: no deja pedir agotados y avisa si algo se agota en el carrito.
 *
 *  Vendedor (CU-04, CU-02):
 *    - CU-04: marca productos de su chaza como disponibles/agotados.
 *    - CU-02: ve la cola de pedidos, avanza el estado y marca listo.
 *    - Refresca cola e inventario periódicamente.
 */
(function () {
    'use strict';

    const INTERVALO = Number(document.body.dataset.intervalo) || 5000;
    const CANTIDAD_MAXIMA = 20;
    const formato = new Intl.NumberFormat('es-CO');
    const precio = (v) => '$ ' + formato.format(v);
    const $ = (id) => document.getElementById(id);

    // ---- API -----------------------------------------------------------------
    async function api(url, opciones) {
        const resp = await fetch(url, Object.assign({ cache: 'no-store' }, opciones));
        let datos = null;
        try { datos = await resp.json(); } catch (e) { /* respuesta sin cuerpo */ }
        if (!resp.ok) {
            const error = new Error((datos && datos.mensaje) || ('HTTP ' + resp.status));
            error.status = resp.status;
            error.datos = datos;
            throw error;
        }
        return datos;
    }

    // ---- Estado global -------------------------------------------------------
    let modo = null;                 // 'comprador' | 'vendedor'
    let temporizador = null;         // setTimeout del refresco activo
    const estadoRefresco = $('estado-refresco');
    const estadoIntervalo = $('estado-intervalo');
    estadoIntervalo.textContent = 'Se actualiza cada ' + (INTERVALO / 1000) + ' s';

    function programarRefresco(fn) {
        cancelarRefresco();
        const ciclo = async () => {
            await fn();
            temporizador = setTimeout(ciclo, INTERVALO);
        };
        temporizador = setTimeout(ciclo, INTERVALO);
    }
    function cancelarRefresco() {
        if (temporizador) { clearTimeout(temporizador); temporizador = null; }
    }

    function marcarEstado(texto, esError) {
        estadoRefresco.textContent = texto;
        estadoRefresco.parentElement.classList.toggle('error', Boolean(esError));
    }

    // ============================================================
    //  MODO COMPRADOR
    // ============================================================
    const comprador = {
        productos: new Map(),  // id -> { id, nombre, precio, disponible, chazaId, chazaNombre, elemento }
        carrito: [],           // [{ id, cantidad }]
        enviando: false
    };

    async function entrarComprador() {
        const chazas = await api('/api/chazas');
        const cont = $('chazas-comprador');
        cont.replaceChildren();
        comprador.productos.clear();

        for (const chaza of chazas) {
            const menu = await api('/api/chazas/' + chaza.id + '/menu');
            cont.append(construirChaza(chaza, menu));
        }
        dibujarCarrito();
        programarRefresco(refrescarDisponibilidadComprador);
        marcarEstado('Menús cargados');
    }

    function construirChaza(chaza, menu) {
        const ventana = document.createElement('section');
        ventana.className = 'ventana chaza-bloque';

        const titulo = document.createElement('div');
        titulo.className = 'ventana-titulo chaza-encabezado';
        const nombre = document.createElement('span');
        nombre.textContent = chaza.nombre;
        const ubic = document.createElement('span');
        ubic.className = 'chaza-ubicacion';
        ubic.textContent = chaza.ubicacion;
        titulo.append(nombre, ubic);
        ventana.append(titulo);

        if (chaza.imagenUrl) {
            const img = document.createElement('img');
            img.className = 'banner-chico';
            img.src = chaza.imagenUrl;
            img.alt = chaza.nombre;
            ventana.append(img);
        }

        for (const cat of menu.categorias) {
            const h = document.createElement('div');
            h.className = 'categoria-nombre';
            h.textContent = cat.nombre;
            ventana.append(h);

            const ul = document.createElement('ul');
            ul.className = 'lista-productos';
            for (const p of cat.productos) {
                ul.append(filaProducto(p, chaza));
            }
            ventana.append(ul);
        }
        return ventana;
    }

    function filaProducto(p, chaza) {
        const li = document.createElement('li');
        li.className = 'producto' + (p.disponible ? '' : ' agotado');

        const info = document.createElement('div');
        info.className = 'producto-info';
        const nom = document.createElement('div');
        nom.className = 'producto-nombre';
        nom.textContent = p.nombre;
        const desc = document.createElement('div');
        desc.className = 'producto-descripcion';
        desc.textContent = p.descripcion || '';
        info.append(nom, desc);

        const pre = document.createElement('div');
        pre.className = 'producto-precio';
        pre.textContent = precio(p.precio);

        const estado = document.createElement('span');
        estado.className = 'estado ' + (p.disponible ? 'estado-ok' : 'estado-agotado');
        estado.textContent = p.disponible ? 'Disponible' : 'Agotado';

        const boton = document.createElement('button');
        boton.type = 'button';
        boton.className = 'boton agregar';
        boton.textContent = p.disponible ? 'Agregar' : 'No disponible';
        boton.disabled = !p.disponible;
        boton.addEventListener('click', () => agregarAlCarrito(p.id));

        li.append(info, pre, estado, boton);

        comprador.productos.set(p.id, {
            id: p.id, nombre: p.nombre, precio: p.precio, disponible: p.disponible,
            chazaId: chaza.id, chazaNombre: chaza.nombre, elemento: li, estadoEl: estado, botonEl: boton
        });
        return li;
    }

    function agregarAlCarrito(id) {
        const prod = comprador.productos.get(id);
        if (!prod || !prod.disponible) return;
        const item = comprador.carrito.find((i) => i.id === id);
        if (item) {
            if (item.cantidad >= CANTIDAD_MAXIMA) {
                mensajeCarrito('aviso', 'La cantidad máxima por producto es ' + CANTIDAD_MAXIMA + '.');
                return;
            }
            item.cantidad++;
        } else {
            comprador.carrito.push({ id, cantidad: 1 });
        }
        ocultarMensajeCarrito();
        dibujarCarrito();
    }

    function cambiarCantidad(id, delta) {
        const item = comprador.carrito.find((i) => i.id === id);
        if (!item) return;
        const prod = comprador.productos.get(id);
        if (delta > 0 && (!prod.disponible || item.cantidad >= CANTIDAD_MAXIMA)) return;
        item.cantidad += delta;
        if (item.cantidad <= 0) comprador.carrito = comprador.carrito.filter((i) => i.id !== id);
        dibujarCarrito();
    }

    function quitarDelCarrito(id) {
        comprador.carrito = comprador.carrito.filter((i) => i.id !== id);
        dibujarCarrito();
    }

    function botonMini(texto, titulo, accion, deshabilitado) {
        const b = document.createElement('button');
        b.type = 'button';
        b.className = 'boton mini';
        b.textContent = texto;
        b.title = titulo;
        b.setAttribute('aria-label', titulo);
        b.disabled = Boolean(deshabilitado);
        b.addEventListener('click', accion);
        return b;
    }

    function dibujarCarrito() {
        const cont = $('carrito-grupos');
        cont.replaceChildren();

        // Agrupar por chaza para que se note que se arma un pedido por chaza.
        const grupos = new Map();  // chazaId -> { nombre, items: [] }
        for (const item of comprador.carrito) {
            const prod = comprador.productos.get(item.id);
            if (!prod) continue;
            if (!grupos.has(prod.chazaId)) grupos.set(prod.chazaId, { nombre: prod.chazaNombre, items: [] });
            grupos.get(prod.chazaId).items.push({ item, prod });
        }

        let total = 0;
        const agotados = [];
        for (const [, grupo] of grupos) {
            const bloque = document.createElement('div');
            bloque.className = 'carrito-grupo';
            const cab = document.createElement('div');
            cab.className = 'carrito-grupo-titulo';
            cab.textContent = grupo.nombre;
            bloque.append(cab);

            for (const { item, prod } of grupo.items) {
                const subtotal = prod.precio * item.cantidad;
                total += subtotal;
                if (!prod.disponible) agotados.push(prod.nombre);

                const li = document.createElement('div');
                li.className = 'carrito-item' + (prod.disponible ? '' : ' agotado');

                const fila = document.createElement('div');
                fila.className = 'carrito-fila';
                const nombre = document.createElement('span');
                nombre.className = 'carrito-nombre';
                nombre.textContent = prod.nombre;
                const sub = document.createElement('span');
                sub.textContent = precio(subtotal);
                fila.append(nombre, sub);

                const controles = document.createElement('div');
                controles.className = 'carrito-controles';
                const cantidad = document.createElement('span');
                cantidad.className = 'carrito-cantidad';
                cantidad.textContent = item.cantidad;
                controles.append(
                    botonMini('−', 'Quitar uno', () => cambiarCantidad(item.id, -1)),
                    cantidad,
                    botonMini('+', 'Agregar uno', () => cambiarCantidad(item.id, 1),
                        !prod.disponible || item.cantidad >= CANTIDAD_MAXIMA),
                    botonMini('Quitar', 'Quitar del carrito', () => quitarDelCarrito(item.id))
                );
                if (!prod.disponible) {
                    const et = document.createElement('span');
                    et.className = 'estado estado-agotado';
                    et.textContent = 'Agotado';
                    controles.append(et);
                }
                li.append(fila, controles);
                bloque.append(li);
            }
            cont.append(bloque);
        }

        $('carrito-vacio').hidden = comprador.carrito.length > 0;
        $('carrito-total').textContent = precio(total);

        if (agotados.length > 0) {
            mensajeCarrito('aviso', (agotados.length === 1 ? 'Se agotó: ' : 'Se agotaron: ')
                + agotados.join(', ') + '. Quítalos del carrito para poder confirmar.', 'agotados');
        } else if ($('mensaje-carrito').dataset.origen === 'agotados') {
            ocultarMensajeCarrito();
        }

        const boton = $('confirmar-compra');
        boton.disabled = comprador.enviando || comprador.carrito.length === 0 || agotados.length > 0;
        boton.textContent = comprador.enviando ? 'Enviando…' : 'Confirmar pedido';
    }

    function mensajeCarrito(tipo, texto, origen) {
        const m = $('mensaje-carrito');
        m.className = 'mensaje ' + tipo;
        m.textContent = texto;
        m.dataset.origen = origen || '';
        m.hidden = false;
    }
    function ocultarMensajeCarrito() {
        const m = $('mensaje-carrito');
        m.hidden = true; m.textContent = ''; m.dataset.origen = '';
    }

    function aplicarDisponibilidad(lista) {
        for (const { productoId, disponible } of lista) {
            const prod = comprador.productos.get(productoId);
            if (!prod || prod.disponible === disponible) continue;
            prod.disponible = disponible;
            prod.elemento.classList.toggle('agotado', !disponible);
            prod.estadoEl.textContent = disponible ? 'Disponible' : 'Agotado';
            prod.estadoEl.className = 'estado ' + (disponible ? 'estado-ok' : 'estado-agotado');
            prod.botonEl.disabled = !disponible;
            prod.botonEl.textContent = disponible ? 'Agregar' : 'No disponible';
            prod.elemento.classList.add('cambio');
            setTimeout(() => prod.elemento.classList.remove('cambio'), 1500);
        }
        dibujarCarrito();
    }

    async function refrescarDisponibilidadComprador() {
        // Agrupar productos por chaza y consultar cada una.
        const chazas = new Set();
        for (const prod of comprador.productos.values()) chazas.add(prod.chazaId);
        try {
            for (const chazaId of chazas) {
                const lista = await api('/api/chazas/' + chazaId + '/disponibilidad');
                aplicarDisponibilidad(lista);
            }
            marcarEstado('Disponibilidad actualizada a las ' + new Date().toLocaleTimeString('es-CO'));
        } catch (e) {
            marcarEstado('No se pudo actualizar la disponibilidad. Reintentando…', true);
        }
    }

    async function confirmarCompra() {
        const nombre = $('nombre-estudiante').value.trim();
        if (!nombre) {
            mensajeCarrito('error', 'Escribe tu nombre para identificar el pedido.');
            $('nombre-estudiante').focus();
            return;
        }
        comprador.enviando = true;
        dibujarCarrito();
        try {
            const resultado = await api('/api/compras', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    nombreEstudiante: nombre,
                    items: comprador.carrito.map((i) => ({ productoId: i.id, cantidad: i.cantidad }))
                })
            });
            comprador.carrito = [];
            $('nombre-estudiante').value = '';
            ocultarMensajeCarrito();
            dibujarCarrito();
            mostrarConfirmacion(resultado);
            await refrescarDisponibilidadComprador();
        } catch (e) {
            if (e.status === 409 && e.datos && Array.isArray(e.datos.productosAgotados)) {
                aplicarDisponibilidad(e.datos.productosAgotados.map((p) => ({ productoId: p.productoId, disponible: false })));
            } else {
                mensajeCarrito('error', e.message || 'No se pudo registrar el pedido.');
            }
        } finally {
            comprador.enviando = false;
            dibujarCarrito();
        }
    }

    function mostrarConfirmacion(resultado) {
        const cont = $('compra-confirmada');
        cont.replaceChildren();
        cont.hidden = false;

        const ok = document.createElement('div');
        ok.className = 'mensaje exito';
        ok.textContent = resultado.pedidos.length === 1
            ? 'Tu pedido quedó en la cola de la chaza.'
            : 'Tus pedidos quedaron en la cola de cada chaza.';
        cont.append(ok);

        for (const pedido of resultado.pedidos) {
            const bloque = document.createElement('div');
            bloque.className = 'confirmacion-pedido';
            const titulo = document.createElement('div');
            titulo.className = 'confirmacion-titulo';
            // Aquí se nota el caso de uso: el pedido entra a la cola con su estado.
            titulo.textContent = 'Pedido #' + pedido.id + ' · ' + pedido.chazaNombre + ' · ' + pedido.estado;
            bloque.append(titulo);

            const ul = document.createElement('ul');
            ul.className = 'confirmacion-items';
            for (const it of pedido.items) {
                const li = document.createElement('li');
                li.textContent = it.cantidad + ' × ' + it.producto + '  (' + precio(it.subtotal) + ')';
                ul.append(li);
            }
            const tot = document.createElement('div');
            tot.className = 'confirmacion-total';
            tot.textContent = 'Total: ' + precio(pedido.total);
            bloque.append(ul, tot);
            cont.append(bloque);
        }
        cont.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }

    // ============================================================
    //  MODO VENDEDOR
    // ============================================================
    const vendedor = { chazaId: null };

    async function entrarVendedor() {
        const selector = $('selector-chaza');
        if (!selector.dataset.cargado) {
            const chazas = await api('/api/chazas');
            selector.replaceChildren();
            for (const c of chazas) {
                const op = document.createElement('option');
                op.value = c.id;
                op.textContent = c.nombre;
                selector.append(op);
            }
            selector.dataset.cargado = '1';
            selector.addEventListener('change', () => seleccionarChazaVendedor(Number(selector.value)));
        }
        await seleccionarChazaVendedor(Number(selector.value));
    }

    async function seleccionarChazaVendedor(chazaId) {
        vendedor.chazaId = chazaId;
        ocultarMensajeVendedor();
        await Promise.all([dibujarInventario(), dibujarCola()]);
        programarRefresco(refrescarVendedor);
        marcarEstado('Panel del vendedor cargado');
    }

    async function refrescarVendedor() {
        try {
            await Promise.all([dibujarInventario(), dibujarCola()]);
            marcarEstado('Panel actualizado a las ' + new Date().toLocaleTimeString('es-CO'));
        } catch (e) {
            marcarEstado('No se pudo actualizar el panel. Reintentando…', true);
        }
    }

    // CU-04: inventario con botones de disponibilidad
    async function dibujarInventario() {
        const menu = await api('/api/chazas/' + vendedor.chazaId + '/menu');
        const cont = $('inventario-vendedor');
        cont.replaceChildren();

        for (const cat of menu.categorias) {
            const h = document.createElement('div');
            h.className = 'categoria-nombre';
            h.textContent = cat.nombre;
            cont.append(h);

            const ul = document.createElement('ul');
            ul.className = 'lista-productos';
            for (const p of cat.productos) {
                const li = document.createElement('li');
                li.className = 'producto' + (p.disponible ? '' : ' agotado');

                const info = document.createElement('div');
                info.className = 'producto-info';
                const nom = document.createElement('div');
                nom.className = 'producto-nombre';
                nom.textContent = p.nombre;
                info.append(nom);

                const estado = document.createElement('span');
                estado.className = 'estado ' + (p.disponible ? 'estado-ok' : 'estado-agotado');
                estado.textContent = p.disponible ? 'Disponible' : 'Agotado';

                const boton = document.createElement('button');
                boton.type = 'button';
                boton.className = 'boton' + (p.disponible ? '' : ' boton-primario');
                boton.textContent = p.disponible ? 'Marcar agotado' : 'Reactivar';
                boton.addEventListener('click', () => cambiarDisponibilidad(p.id, !p.disponible));

                li.append(info, estado, boton);
                ul.append(li);
            }
            cont.append(ul);
        }
    }

    async function cambiarDisponibilidad(productoId, disponible) {
        try {
            await api('/api/chazas/' + vendedor.chazaId + '/productos/' + productoId + '/disponibilidad', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ disponible })
            });
            ocultarMensajeVendedor();
            await dibujarInventario();
        } catch (e) {
            mensajeVendedor('error', e.message || 'No se pudo cambiar la disponibilidad.');
        }
    }

    // CU-02: cola de pedidos
    async function dibujarCola() {
        const cola = await api('/api/chazas/' + vendedor.chazaId + '/cola');

        const pendientes = $('cola-pendientes');
        pendientes.replaceChildren();
        $('cola-vacia').hidden = cola.pendientes.length > 0;
        cola.pendientes.forEach((p, i) => pendientes.append(tarjetaPedido(p, i + 1)));

        const listos = $('cola-listos');
        listos.replaceChildren();
        $('listos-vacio').hidden = cola.listos.length > 0;
        cola.listos.forEach((p) => listos.append(tarjetaPedido(p, null)));
    }

    function tarjetaPedido(pedido, turno) {
        const li = document.createElement('li');
        li.className = 'pedido-cola';

        const cab = document.createElement('div');
        cab.className = 'pedido-cab';
        const titulo = document.createElement('span');
        titulo.className = 'pedido-titulo';
        titulo.textContent = (turno ? turno + '. ' : '') + pedido.nombreEstudiante + ' · #' + pedido.id;
        const estado = document.createElement('span');
        estado.className = 'estado estado-info';
        estado.textContent = pedido.estado;
        cab.append(titulo, estado);

        const hora = document.createElement('div');
        hora.className = 'pedido-hora';
        hora.textContent = 'Llegó: ' + new Date(pedido.fechaHora).toLocaleString('es-CO');

        const items = document.createElement('ul');
        items.className = 'pedido-items';
        for (const it of pedido.items) {
            const i = document.createElement('li');
            i.textContent = it.cantidad + ' × ' + it.producto;
            items.append(i);
        }

        const total = document.createElement('div');
        total.className = 'pedido-total';
        total.textContent = 'Total: ' + precio(pedido.total);

        li.append(cab, hora, items, total);

        // Acciones CU-02-HU-02 / HU-03
        const acciones = document.createElement('div');
        acciones.className = 'acciones';
        if (pedido.puedeMarcarListo) {
            acciones.append(crearAccion('Marcar listo', 'listo', pedido.id));
        }
        if (pedido.puedeAvanzar && !pedido.puedeMarcarListo) {
            acciones.append(crearAccion('Pasar a ' + pedido.siguienteEstado, 'avanzar', pedido.id));
        }
        if (acciones.childElementCount > 0) li.append(acciones);

        return li;
    }

    function crearAccion(texto, tipo, pedidoId) {
        const b = document.createElement('button');
        b.type = 'button';
        b.className = 'boton boton-primario';
        b.textContent = texto;
        b.addEventListener('click', async () => {
            b.disabled = true;
            try {
                await api('/api/chazas/' + vendedor.chazaId + '/pedidos/' + pedidoId + '/' + tipo, { method: 'POST' });
                ocultarMensajeVendedor();
                await dibujarCola();
            } catch (e) {
                mensajeVendedor('error', e.message || 'No se pudo actualizar el pedido.');
                b.disabled = false;
            }
        });
        return b;
    }

    function mensajeVendedor(tipo, texto) {
        const m = $('mensaje-vendedor');
        m.className = 'mensaje ' + tipo;
        m.textContent = texto;
        m.hidden = false;
    }
    function ocultarMensajeVendedor() {
        const m = $('mensaje-vendedor');
        m.hidden = true; m.textContent = '';
    }

    // ============================================================
    //  Cambio de modo
    // ============================================================
    async function cambiarModo(nuevo) {
        if (modo === nuevo) return;
        modo = nuevo;
        cancelarRefresco();

        $('pantalla-inicio').hidden = true;
        $('selector-modo').hidden = false;
        document.querySelectorAll('#selector-modo .modo').forEach((b) => {
            b.classList.toggle('activo', b.dataset.modo === nuevo);
        });

        $('pantalla-comprador').hidden = nuevo !== 'comprador';
        $('pantalla-vendedor').hidden = nuevo !== 'vendedor';

        try {
            if (nuevo === 'comprador') await entrarComprador();
            else await entrarVendedor();
        } catch (e) {
            marcarEstado('No se pudo cargar: ' + (e.message || 'error'), true);
        }
    }

    // ---- Eventos de arranque -------------------------------------------------
    document.querySelectorAll('[data-modo]').forEach((el) => {
        el.addEventListener('click', () => cambiarModo(el.dataset.modo));
    });
    $('confirmar-compra').addEventListener('click', confirmarCompra);
})();
