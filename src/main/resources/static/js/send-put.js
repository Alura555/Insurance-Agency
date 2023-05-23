function sendPutRequest(url, id, formId) {
    var form = document.getElementById(formId);
    var formData = new FormData(form);

    fetch(url + '/' + id, {
      method: 'PUT',
      body: formData
    })
    .then(function(response) {
      if (response.ok) {
        return response.text();
      } else {
        throw new Error('Error occurred');
      }
    })
    .then(function(newId) {
        window.location.href = url + '/' + newId;
    });
}
