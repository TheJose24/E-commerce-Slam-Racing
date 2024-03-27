
const btnAbrirCarrito = document.getElementById('btn-abrirCarrito');
const btnCerrarCarrito = document.getElementById('btn-cerrarCarrito');
const contenedorModal = document.getElementById('contenedor-modal');
const contenedorCarrito = document.getElementById('contenedor-carrito');

btnAbrirCarrito.addEventListener('click', function() {
    contenedorModal.classList.remove('-z-10');
    contenedorModal.classList.add('z-10');
    contenedorModal.classList.add('block');
    contenedorModal.classList.add('opacity-100');
    contenedorCarrito.classList.remove('translate-x-full');
    contenedorCarrito.classList.add('translate-x-0');
});

btnCerrarCarrito.addEventListener('click', function() {
    contenedorCarrito.classList.remove('translate-x-0');
    contenedorCarrito.classList.add('translate-x-full');
    contenedorModal.classList.remove('opacity-100');
    setTimeout(function() {
        contenedorModal.classList.remove('z-10');
        contenedorModal.classList.add('-z-10');
    }, 500);
});

document.addEventListener('DOMContentLoaded', function () {
    cargarCarrito();
});

// variable que almacenara el pedido
let pedido;
let detalles;
function cargarCarrito(){

    fetch('/v1/carrito/verCarrito')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener el carrito');
            }
            return response.json();
        })
        .then(data => {
            pedido = data;
            console.log(data.detalles);
            detalles = data.detalles;

            const subtotalText = document.getElementById('subtotal');
            const envioText = document.getElementById('envio');
            const totalText = document.getElementById('total');

            if (!data.detalles || data.detalles.length === 0) {
                // Si no hay detalles en el carrito, mostrar un mensaje
                const mensaje = document.createElement('p');
                mensaje.classList.add('text-center', 'text-lg');
                mensaje.textContent = 'No tienes nada en tu carrito aún.';
                const productosEnCarritoContainer = document.getElementById('productosEnCarritoContainer');
                productosEnCarritoContainer.innerHTML = ''; // Limpiar el contenedor
                productosEnCarritoContainer.appendChild(mensaje);
                subtotalText.innerText = '0.00';
                envioText.innerText = '0.00';
                totalText.innerText = '0.00';
                return;
            }
            let subtotal = data.subTotal;
            let envio = data.envio;
            let total = data.precioTotal;
            // Maneja la respuesta del servidor y agrega los productos al contenedor
            const productosEnCarritoContainer = document.getElementById('productosEnCarritoContainer');
            productosEnCarritoContainer.innerHTML = ''; // Limpiar el contenedor


            data.detalles.forEach(producto => {
                const divProducto = document.createElement('div');
                divProducto.classList.add('producto');
                divProducto.innerHTML = `
                        <div class="flex justify-between w-full items-center px-2 py-1.5 border border-solid
                         border-black rounded-2xl">
                            <div class="flex items-center gap-x-4">
                                <img class="object-contain w-[70px] bg-lightGray200 rounded-lg"
                                     src="/images/${producto.producto.imagenes[0].url}" alt="${producto.producto.slug}">
                                <div class="flex flex-col">
                                    <h2 class="text-lg font-semibold">${producto.producto.nombre} ${producto.producto.color}</h2>
                                    <h3 class="text-sm">${producto.producto.material} ™</h3>
                                </div>
                            </div>
                            <div class="inline-flex justify-between w-9">
                                <span class="cantidad-producto" data-id="${producto.producto.id}">${producto.cantidad}</span>
                                <div class="flex flex-col gap-y-1">
                                    <button class="btn-aumentar">
                                        <svg width="22" height="8" viewBox="0 0 22 8" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M21.2501 7.82591L11.0527 0.0919189L0.855347 7.82591H21.2501Z" fill="black"/>
                                        </svg>
                                    </button>
                                    <button class="btn-disminuir">
                                        <svg width="22" height="9" viewBox="0 0 22 9" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M21.2501 0.40395L11.0527 8.13794L0.855347 0.40395H21.2501Z" fill="black"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                            <div>
                                <p class="text-lg font-semibold">S/. ${producto.producto.precio_unitario-(producto.producto.precio_unitario*(producto.producto.descuento/100))}</p>
                                <div class="btn-eliminar flex items-center justify-center cursor-pointer"
                                data-id="${producto.producto.id}">
                                    <svg class="w-3 h-3 mr-1" xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none">
                                        <path d="M19.5 5.5L18.8803 15.5251C18.7219 18.0864 18.6428 19.3671 18.0008 20.2879C17.6833 20.7431 17.2747 21.1273 16.8007 21.416C15.8421 22 14.559 22 11.9927 22C9.42312 22 8.1383 22 7.17905 21.4149C6.7048 21.1257 6.296 20.7408 5.97868 20.2848C5.33688 19.3626 5.25945 18.0801 5.10461 15.5152L4.5 5.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                                        <path d="M3 5.5H21M16.0557 5.5L15.3731 4.09173C14.9196 3.15626 14.6928 2.68852 14.3017 2.39681C14.215 2.3321 14.1231 2.27454 14.027 2.2247C13.5939 2 13.0741 2 12.0345 2C10.9688 2 10.436 2 9.99568 2.23412C9.8981 2.28601 9.80498 2.3459 9.71729 2.41317C9.32164 2.7167 9.10063 3.20155 8.65861 4.17126L8.05292 5.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                                        <path d="M9.5 16.5L9.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                                        <path d="M14.5 16.5L14.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                                    </svg>
                                    <p class="text-xs text-red">Eliminar</p>
                                </div>
                            </div>
                        </div>
                    `;
                productosEnCarritoContainer.appendChild(divProducto);
            });
            subtotalText.innerText = subtotal.toFixed(2);
            envioText.innerText = envio;
            totalText.innerText = total.toFixed(2);


            // Añadir funcionalidad a los botones de aumentar y disminuir
            const btnAumentar = document.querySelectorAll('.btn-aumentar');
            const btnDisminuir = document.querySelectorAll('.btn-disminuir');
            const cantidadProducto = document.querySelectorAll('.cantidad-producto');

            btnAumentar.forEach(btn => {
                btn.addEventListener('click', function() {
                    let index = Array.from(btnAumentar).indexOf(btn);
                    let cantidad = parseInt(cantidadProducto[index].innerText);
                    cantidad++;
                    cantidadProducto[index].innerText = cantidad;
                    subtotalText.innerText = (parseFloat(subtotalText.innerText) + parseFloat(detalles[index].producto.precio_unitario-(detalles[index].producto.precio_unitario*(detalles[index].producto.descuento/100)))).toFixed(2);
                    totalText.innerText = (parseFloat(subtotalText.innerText) + parseFloat(envioText.innerText)).toFixed(2);
                });
            });

            btnDisminuir.forEach(btn => {
                btn.addEventListener('click', function() {
                    let index = Array.from(btnDisminuir).indexOf(btn);
                    let cantidad = parseInt(cantidadProducto[index].innerText);
                    if (cantidad > 1) {
                        cantidad--;
                        cantidadProducto[index].innerText = cantidad;
                    }
                    subtotalText.innerText = (parseFloat(subtotalText.innerText) - parseFloat(detalles[index].producto.precio_unitario-(detalles[index].producto.precio_unitario*(detalles[index].producto.descuento/100)))).toFixed(2);
                    totalText.innerText = (parseFloat(subtotalText.innerText) + parseFloat(envioText.innerText)).toFixed(2);
                });
            });

            // Seleccionar todos los botones de eliminar
            const btnsEliminar = document.querySelectorAll('.btn-eliminar');

            // Iterar sobre cada botón de eliminar
            btnsEliminar.forEach(btnEliminar => {
                // Obtener el ID del producto del atributo "data-id"
                const idProducto = btnEliminar.dataset.id;
                console.log("idProducto: ", idProducto);

                // Agregar un evento de clic a cada botón de eliminar
                btnEliminar.addEventListener('click', function() {
                    // Realizar la solicitud de eliminación del producto
                    fetch('/v1/carrito/eliminarProducto/' + idProducto, {
                        method: 'DELETE',
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Error al eliminar el producto');
                            }
                            // Recargar el carrito después de eliminar el producto
                            cargarCarrito();
                            console.log('Producto eliminado');
                        })
                        .catch(error => {
                            // Manejar cualquier error que ocurra durante la solicitud
                            console.error('Error:', error);
                        });
                });
            });


        })
        .catch(error => {
            // Maneja cualquier error que ocurra durante la solicitud
            console.error('Error:', error);
        });

}

// Ir a la página de pago para empezar el proceso
const btnPagar = document.getElementById('btn-procesoPago');
btnPagar.addEventListener('click', function () {

    if (!pedido.detalles || pedido.detalles.length === 0) {
        // Si no hay detalles en el carrito, mostrar un mensaje
        alert('No tienes nada en tu carrito aún.');
        return;
    }

    let cantidadesProductos  = document.querySelectorAll('.cantidad-producto');

    // Iterar sobre cada elemento de la lista
    cantidadesProductos.forEach(cantidadProducto => {
        // Obtener la cantidad de productos de este elemento
        let cantidad = cantidadProducto.innerText;

        // Obtener el id del producto asociado a este elemento
        let idProducto = cantidadProducto.dataset.id;

        // Iterar sobre los detalles del pedido
        pedido.detalles.forEach(producto => {
            // Verificar si el id del producto coincide con el id obtenido del botón
            if (producto.producto.id === parseInt(idProducto)) {
                // Actualizar la cantidad de productos para este producto
                producto.cantidad = parseInt(cantidad);
                // Actualizar el precio total para este producto
                producto.total = (producto.producto.precio_unitario - (producto.producto.precio_unitario * (producto.producto.descuento / 100))) * cantidad;
            }
        });
        const subtotalText = document.getElementById('subtotal').textContent;
        const totalText = document.getElementById('total').textContent;
        // Actualizar el subtotal y el precio total del pedido
        pedido.subTotal = parseFloat(subtotalText).toFixed(2);
        pedido.precioTotal = parseFloat(totalText).toFixed(2);


    });

    // Realizar otra solicitud para ir a la página de pago
    fetch('/v1/carrito/pagar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedido)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener el pedido para el pago');
            }
            // Redirigir a la página de pago renderizada por thymeleaf
            window.location.href = '/procesoPago';
        })
        .catch(error => {
            // Maneja cualquier error que ocurra durante la solicitud
            console.error('Error:', error);
        });

});






