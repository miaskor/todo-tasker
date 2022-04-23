function createDivForExistTask(date, item) {
  const tasksDiv = document.getElementById(date).getElementsByClassName(
          'tasks')[0],
      taskDiv = new Div(item['id'], 'task'),
      taskValueDiv = new Div('', 'value_task'),
      task = new InputElement('text', '', item['taskName'], '', '', false,
          '');
  setColorByState(taskValueDiv.divElement, item["taskState"]);
  taskDiv.renderAppend(tasksDiv);
  if (item['taskName'].length > MIN_LENGTH_FOR_TEXT_AREA) {
    isMatchedValueForTextArea(task.inputElement.value, taskDiv.divElement,
        item["taskState"]);
  }
  taskValueDiv.renderAppend(taskDiv.divElement);
  task.renderAppend(taskValueDiv.divElement);
  task.inputElement.disabled = true;

  createAlternationDiv(taskDiv.divElement, item["taskState"]);
}

function createDivForTask(date) {
  const tasksDiv = document.getElementById(date).getElementsByClassName(
          'tasks')[0],
      taskDiv = new Div('', 'task'),
      taskValueDiv = new Div('', 'value_task'),
      task = new InputElement('text', '', '', '', '', false, '');
  taskDiv.renderAppend(tasksDiv);
  taskValueDiv.renderAppend(taskDiv.divElement);
  task.renderAppend(taskValueDiv.divElement);
  task.inputElement.onblur = () => {
    isMatchedValueForTextArea(task.inputElement.value, taskDiv.divElement);
    save();
  };

  task.inputElement.addEventListener('keydown', saveWhenPressEnter);
  task.inputElement.focus();

  function save() {
    if (task.inputElement.value.length !== 0) {
      task.inputElement.onblur = () => {
      };
      task.inputElement.disabled = true;
      saveTask(date, task.inputElement.value, "UPCOMING")

      task.inputElement.removeEventListener('keydown',
          saveWhenPressEnter);
    }
  }

  function saveWhenPressEnter(event) {
    if (event.key === "Enter") {
      isMatchedValueForTextArea(task.inputElement.value,
          taskDiv.divElement);
      save();
    }
  }
}

function createTask(date) {
  let tasksValue = null;
  if (allTasks.hasOwnProperty(date)) {
    tasksValue = allTasks[date].sort(function (a, b) {
      if (a.id > b.id) {
        return 1
      }
      if (a.id < b.id) {
        return -1
      }
      return 0
    });
    for (let index = 0; index < tasksValue.length; index++) {
      createDivForExistTask(date, tasksValue[index]);
    }

  }
  if (date >= new Date().formatToDMY()) {
    createDivForTask(date);
  }
}
