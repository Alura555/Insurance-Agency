  function sendDelete(url, id, confirmationMessage) {
    if (confirm(confirmationMessage)) {
      $.ajax({
        url: url + '/' + id,
        type: 'DELETE',
        success: function(response) {
          window.location.href = url
        }
      });
    }
  }