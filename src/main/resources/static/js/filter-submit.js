 document.getElementById('sort').addEventListener('change', function() {
    document.getElementById('filter').submit();
 });

 const inputs = document.querySelectorAll('.filter-box');

  inputs.forEach(input => {
    input.addEventListener('change', function() {
      document.getElementById('filter').submit();
    });
  });