const groupBy = function (xs, key) {
  return xs.reduce(function (rv, x) {
    (rv[x[key]] = rv[x[key]] || []).push(x);
    return rv;
  }, {});
};

const from = Date.now(),
    to = Date.now(),
    states_list = document.getElementById("state_list"),
    MIN_LENGTH_FOR_TEXT_AREA = 30;

const allTaskByStates = []

const popup = document.querySelector(".popup__overlay");
const btn = document.querySelector(".box-btn");
const close = document.querySelector(".close");
const creationForm = document.querySelector(".creation-form");

btn.addEventListener("click", function (event) {
  event.preventDefault();
  popup.classList.remove("hidden");
});

popup.addEventListener("click", function (event) {
  e = event || window.event
  if (e.target == this) {
    popup.classList.add("hidden");
  }
});

creationForm.addEventListener('submit', (e) => {
  e.preventDefault();
  const data = {};
  new FormData(creationForm).forEach((item, key) => {
    data[key] = item;
  });
  saveTask(data['date'], data['task_name'], data['task_state']).then(
      response => {
        createDivForExistTask(data['task_state'], response)
      })

})

close.addEventListener("click", function (event) {
  event.preventDefault();
  popup.classList.add("hidden");
});

function createDivForExistTask(state, item) {
  const tasksDiv = document.getElementById(state).getElementsByClassName(
          'tasks')[0],
      taskDiv = new Div(item['id'], 'task'),
      taskValueDiv = new Div('', 'value_task'),
      task = new InputElement('text', '', item['taskName'], '', '', false,
          '');
  taskDiv.renderAppend(tasksDiv);
  if (item['taskName'].length > MIN_LENGTH_FOR_TEXT_AREA) {
    isMatchedValueForTextArea(task.inputElement.value, taskDiv.divElement);
  }
  addDroppable(tasksDiv);
  addDraggable(taskValueDiv.divElement);
  taskValueDiv.renderAppend(taskDiv.divElement);
  task.renderAppend(taskValueDiv.divElement);
  task.inputElement.disabled = true;

  createAlternationDiv(taskDiv.divElement, item["taskState"]);
}

function initStateBlocks() {
  const groupedByState = allTaskByStates.reduce((r, a) => {
    r[a.taskState] = [...r[a.taskState] || [], a];
    return r;
  }, {});
  for (let key in groupedByState) {
    for (let i = 0; i < groupedByState[key].length; i++) {
      createDivForExistTask(key, groupedByState[key][i]);
    }
  }
}

getTasksAll(allTaskByStates).then(() => {
  initStateBlocks();
});
