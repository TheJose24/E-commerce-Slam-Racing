// Obtiene todos los botones de edición por su clase CSS
var editButtons = document.querySelectorAll('.editar-btn');

var productId;

var isFormOpen = false;

// Itera sobre cada botón de edición y agrega un controlador de eventos clic
editButtons.forEach(function (button) {
    button.addEventListener('click', function () {

        if (!isFormOpen) {

            console.log('Botón de edición clicado ' + isFormOpen);
            // Obtén el ID de la fila asociado con el botón de edición
            productId = button.closest('tr').id;

            console.log(productId);

            // Encuentra el formulario de edición correspondiente al producto
            var formularioEdicion = document.getElementById('formulario-edicion-' + productId);
            console.log(formularioEdicion);

            // Muestra el formulario de edición
            formularioEdicion.classList.remove('hidden');

            // Agrega un controlador de eventos para el botón de cancelar
            var cancelButton = formularioEdicion.querySelector('.cancelar-btn');
            cancelButton.addEventListener('click', function () {
                // Itera sobre los elementos de formulario dentro del formulario de edición
                formularioEdicion.querySelectorAll('input, select').forEach(function (input) {
                    // Verifica el tipo de elemento y establece su valor en una cadena vacía o cero
                    if (input.type === 'text') {
                        input.value = ''; // Campo de texto, establece el valor en una cadena vacía
                    } else if (input.type === 'number') {
                        input.value = ''; // Campo numérico, establece el valor en cero
                    }
                });
                // Oculta el formulario de edición si se hace clic en Cancelar
                formularioEdicion.classList.add('hidden');
                isFormOpen = false;
                console.log('Botón de cerrado clicado ' + isFormOpen);
            });

            //========================= Agregar nueva imagen desde la edicion =========================

            // Obtén el botón para agregar una nueva imagen
            var nuevoBotonImagen = formularioEdicion.querySelector('#nuevaImagenBtnEdicion');

            // Inicializa el contador de imágenes
            var contadorImagenesEdicion = formularioEdicion.querySelectorAll('.imagen').length;

            // Agregar un evento de clic al botón para agregar una nueva imagen
            nuevoBotonImagen.addEventListener('click', function(event) {
                event.preventDefault();

                // Obtén el HTML del template de imagen desde el HTML oculto
                var templateImagenHTML = document.getElementById('template-imagen').innerHTML;

                // Crea un nuevo elemento de imagen
                var nuevoElementoImagen = document.createElement('div');
                nuevoElementoImagen.classList.add('flex', 'w-32', 'relative');
                nuevoElementoImagen.innerHTML = templateImagenHTML;

                // Asigna un nuevo ID al nuevo elemento de imagen
                var nuevoIdImagen = 'imagen-' + contadorImagenesEdicion;
                nuevoElementoImagen.querySelector('img').id = nuevoIdImagen;
                nuevoElementoImagen.querySelector('label').setAttribute('for', 'imagenEdicion-' + contadorImagenesEdicion);
                nuevoElementoImagen.querySelector('input').setAttribute('id', 'imagenEdicion-' + contadorImagenesEdicion);
                nuevoElementoImagen.querySelector('input').setAttribute('data-index', contadorImagenesEdicion);

                var padreBoton = nuevoBotonImagen.parentNode;
                // Agrega el nuevo elemento de imagen al contenedor de imágenes
                padreBoton.insertBefore(nuevoElementoImagen, nuevoBotonImagen);

                // Incrementa el contador de imágenes
                contadorImagenesEdicion++;

                // Agrega event listener para el nuevo input de imagen
                asignarEventosImagenes();
                asignarEventosEdicionImagenes();
            });


            isFormOpen = true;
        }
    });
});


// Obtener elementos del DOM
var modalContainer = document.getElementById("modalContainer");
var openModalBtn = document.getElementById("openModalBtn");
var cancelBtn = document.getElementById("cancelBtn");

// Agregar evento al botón para mostrar el modal
openModalBtn.addEventListener("click", function () {
    modalContainer.classList.remove("hidden"); // Mostrar el modal
});

// Agregar evento al botón "Cancelar" para ocultar el modal
cancelBtn.addEventListener("click", function (event) {
    event.preventDefault();
    modalContainer.classList.add("hidden"); // Ocultar el modal
});


// ========================= Agregar nueva imagen =========================
var agregarImagenBtn = document.getElementById('nuevaImagenBtn');

var contadorImagenes = 1;

document.addEventListener('DOMContentLoaded', function () {
    agregarImagenBtn.addEventListener('click', function (event) {
        event.preventDefault();
        agregarNuevaImagen();
    });
    asignarEventosImagenes();
});

function agregarNuevaImagen() {
    // Crear dinámicamente el HTML para la nueva imagen
    var nuevaImagenHtml = `
            <div class="flex flex-col justify-between w-[45%]">
                 <label class="text-sm" for="imagenUrl">
                     Imagen ${contadorImagenes}
                 </label>
                 <div class="flex justify-center items-center border border-solid border-grey rounded-lg relative hover:bg-grey hover:text-white px-4 py-1 text-black text-sm">
                    <p class="nombre-archivo">Agregar imagen</p>
                    <input id="imagenUrl" name="imagen" type="file" accept="image/*" class="imagen-input absolute opacity-0 w-full">
                 </div>
            </div>`;

    // Obtener el elemento del botón "Nueva imagen"
    var botonNuevaImagen = document.querySelector('#nuevaImagenBtn');
    // Insertar el HTML de la nueva imagen justo antes del botón "Nueva imagen"
    botonNuevaImagen.insertAdjacentHTML('beforebegin', nuevaImagenHtml);

    contadorImagenes++;

    asignarEventosImagenes();
}

function asignarEventosImagenes() {
    let inputsImagen = document.querySelectorAll('.imagen-input');

    inputsImagen.forEach(inputsImagen => {
        inputsImagen.addEventListener('change', function () {
            let nombreArchivo = this.files[0].name;
            let nombreArchivoElemento = this.parentElement.querySelector('.nombre-archivo');
            if (nombreArchivoElemento) {
                nombreArchivoElemento.innerText = nombreArchivo;
            }
            console.log(nombreArchivo);
            console.log("Cambio de archivo");
        });
    });
}

//=========================== Previsualizar imagen ============================

function asignarEventosEdicionImagenes() {
    let inputsImagenEdicion = document.querySelectorAll('.imagen-input.edicion');

    inputsImagenEdicion.forEach(function(input) {
        input.addEventListener('change', function(event) {
            let file = event.target.files[0];
            let index = event.target.dataset.index;
            let imgElement = document.getElementById('imagen-' + index);

            if (file) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    imgElement.src = e.target.result;
                    console.log(e.target.result);
                };
                reader.readAsDataURL(file);
                console.log(file);
            }
        });
    });
}
