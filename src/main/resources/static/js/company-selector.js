document.addEventListener('DOMContentLoaded', function() {
        var roleSelect = document.getElementById('role');
        var companySelect = document.getElementById('companySelect');

        roleSelect.addEventListener('change', function() {
            if (roleSelect.value === 'COMPANY MANAGER') {
                companySelect.style.display = 'block';
            } else {
                companySelect.style.display = 'none';
            }
        });
    });