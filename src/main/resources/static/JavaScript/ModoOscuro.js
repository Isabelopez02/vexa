
// Se obtiene el botón Modo Oscuro
const btnModoOscuro = document.getElementById('btnModoOscuro');

// Se obtiene la tabla de tareas
const tabla = document.getElementById('tablaTareas');

// Se añade un "escuchador de eventos"
btnModoOscuro.addEventListener('click', () => {
  
  // Se alterna (agrega o quita) la clase 'modo-oscuro' en el <body>
  document.body.classList.toggle('modo-oscuro');

  // Verifica si la tabla actualmente tiene la clase 'table-dark'
  if (tabla.classList.contains('table-dark')) {
    // Si está en modo oscuro, se cambia a modo claro
    tabla.classList.remove('table-dark');
    tabla.classList.add('table-light');      

    // Cambia el texto del botón para indicar que se puede activar el modo oscuro
    btnModoOscuro.textContent = 'Modo oscuro';

    // Cambia la apariencia del botón al estilo Oscuro
    btnModoOscuro.classList.remove('btn-light');
    btnModoOscuro.classList.add('btn-dark');
  } else {
    // Si está en modo claro, se cambia a modo oscuro
    tabla.classList.remove('table-light'); 
    tabla.classList.add('table-dark');       

    // Cambia el texto del botón para indicar que se puede volver al modo claro
    btnModoOscuro.textContent = 'Modo claro';

    // Cambia la apariencia del botón al estilo claro
    btnModoOscuro.classList.remove('btn-dark');
    btnModoOscuro.classList.add('btn-light');
  }
});



