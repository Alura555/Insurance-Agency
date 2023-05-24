function checkDates() {
  var currentDate = new Date();
    var startDateElement = document.getElementById("startDate");
    if (startDateElement){
        var startDate = new Date(startDateElement.value);
        if (startDate && startDate < currentDate) {
          var startDateError = document.getElementById('startDateError');
          startDateError.textContent = 'Start date must be later than current date.';
          return false;
        }
    }

    var issueDateInputs = document.querySelectorAll("[id^='date-']");
    var hasInvalidDate = false;
    for (var i = 0; i < issueDateInputs.length; i++) {
        if(issueDateInputs[i]){
          var issueDate = new Date(issueDateInputs[i].value);
          if (issueDate && issueDate > currentDate) {
            var issueDateError = document.getElementById('issueDateError-' + issueDateInputs[i].id.replace('date-', ''));
            issueDateError.textContent = 'Issue date of document must be earlier than current date.';
            hasInvalidDate = true;
          }
        }
    }
    if (hasInvalidDate) {
      return false;
    }

    return true;
}