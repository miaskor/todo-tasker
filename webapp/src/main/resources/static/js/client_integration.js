const URN_TO_CLIENTS = `/clients`;

async function getClient(clientId) {
  return await fetch(
      `${URN_TO_CLIENTS}/${clientId}`, {
        method: 'GET',
        headers: {
          'Content-type': 'application/json'
        }
      }).then(response => response.json());
}

async function update(clientId, email = null, login = null, password = null,
    botId = null) {

  if (email == null || login == null || password == null || botId == null) {
    getClient(clientId).then(json => {
      email = json["email"]
      login = json["login"]
      password = json["password"]
      botId = json["botId"]
    });
  }

  return await fetch(
      `${URN_TO_CLIENTS}/update/${clientId}`, {
        method: 'PATCH',
        headers: {
          'Content-type': 'application/json'
        },
        body: JSON.stringify({
          'clientId': getCookie('clientId'),
          'email': email,
          'login': login,
          'password': password,
          'botId': botId
        }),
      }).then(response => response.json());
}
