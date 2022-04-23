const URN_TO_TASKS = `/tasks`;

async function getTasks(dateFrom, dateTo, listTasks) {
  return await fetch(
      `${URN_TO_TASKS}/range?date_from=${dateFrom}&date_to=${dateTo}`, {
        method: 'GET',
        headers: {
          'Content-type': 'application/json'
        }
      }).then(response => response.text())
  .then(text => Object.assign(listTasks, JSON.parse(text)));
}

async function getTasksAll(listTasks) {
  return await fetch(`${URN_TO_TASKS}/all?client_id=${getCookie("clientId")}`, {
    method: 'GET',
    headers: {
      'Content-type': 'application/json'
    }
  }).then(response => response.text())
  .then(text => Object.assign(listTasks, JSON.parse(text)));
}

async function saveTask(date, taskName, taskState) {
  return await fetch(`${URN_TO_TASKS}/save`, {
    method: 'POST',
    headers: {
      'Content-type': 'application/json'
    },
    body: JSON.stringify(
        {
          'clientId': getCookie('clientId'),
          'taskName': taskName,
          'taskState': taskState,
          'date': date
        }),
  }).then(response => response.json())
}

function deleteTask(id) {
  fetch(`${URN_TO_TASKS}/${id}`, {
    method: 'DELETE'
  }).then(response => response.json());
}

function updateTask(id, taskState = null, taskName = null, date = null) {
  fetch(`${URN_TO_TASKS}/update/${id}`, {
    method: 'PUT',
    headers: {
      'Content-type': 'application/json'
    },
    body: JSON.stringify(
        {
          'clientId': getCookie('clientId'),
          'taskName': taskName,
          'taskState': taskState,
          'date': date
        }),
  }).then(response => response.json());
}
