let iconoFavorito = document.querySelectorAll('#icono-favorito');

iconoFavorito.forEach(function (icono) {
    icono.addEventListener('click', function () {

        // Cambiar las clases de Tailwind para cambiar el color del texto del SVG
        if (icono.classList.contains('text-white')) {
            icono.classList.remove('text-white');
            icono.classList.add('text-grey'); // Cambia al color gris deseado
        } else {
            icono.classList.remove('text-grey');
            icono.classList.add('text-white'); // Cambia de nuevo al color original
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const toggleIcons = document.querySelectorAll('.toggle-icon');

    toggleIcons.forEach(icon => {
        icon.addEventListener('click', function () {
            const targetId = this.previousElementSibling.dataset.target;
            const targetOptions = document.querySelector(`[data-target="${targetId}-options"]`);

            targetOptions.classList.toggle('show');
        });
    });
});

function toggleOptions(id) {
    // Ocultar todas las opciones de filtrado
    var allOptions = document.querySelectorAll('.options-container');
    allOptions.forEach(function(option) {
        if (option.id !== id) {
            option.classList.add('hidden');
        }
    });

    // Mostrar las opciones del filtro seleccionado
    var selectedOption = document.getElementById(id);
    selectedOption.classList.toggle('hidden');

}




