function createAlternationDiv(taskDiv, state) {
  const alternationDiv = new Div('', 'alternation_task', 'hidden'),
      deleteImage = document.createElement('img'),
      alternateImage = document.createElement('img'),
      divForDeleteImage = new Div('', 'delete_image'),
      divForAlternateImage = new Div('', 'alternate_image');
  alternationDiv.renderAppend(taskDiv);
  divForDeleteImage.renderAppend(taskDiv);
  divForAlternateImage.renderAppend(taskDiv);
  deleteImage.src = '../img/recycle.png';
  alternateImage.src = '../img/pencil.png';
  divForDeleteImage.divElement.addEventListener('click', () => {
    taskDiv.remove();
    deleteTask(taskDiv.id);
  });
  divForAlternateImage.divElement.addEventListener('click', () => {
    const value_task = taskDiv.getElementsByClassName('value_task')[0],
        input = value_task.getElementsByTagName('input')[0],
        divText = taskDiv.getElementsByClassName("pop_up_task")[0];
    if (divText !== undefined) {
      divText.classList.remove("pop_up_task");
    }

    input.disabled = false;
    input.focus();
    input.onblur = () => {
      whenAlternationIsEnd();
    };
    input.addEventListener('keydown', (event) => {
      if (event.key === "Enter") {
        whenAlternationIsEnd();
      }
    });

    function whenAlternationIsEnd() {
      if (input.value.length === 0) {
        taskDiv.remove();
        deleteTask(taskDiv.id);
      } else {
        input.disabled = true;
        if (input.value.length < MIN_LENGTH_FOR_TEXT_AREA) {
          if (divText !== undefined) {
            divText.remove();
          }
        } else {
          if (divText !== undefined) {
            divText.innerText = input.value;
            divText.classList.add("pop_up_task");
          } else {
            isMatchedValueForTextArea(input.value, taskDiv, state);
          }
        }
        const toDoDay = taskDiv.parentElement.parentElement;
        updateTask(taskDiv.id, state, input.value, toDoDay.id);
      }
    }
  });
  setColorByState(divForAlternateImage.divElement, state)
  setColorByState(alternationDiv.divElement, state)
  divForAlternateImage.divElement.append(alternateImage);
  divForDeleteImage.divElement.append(deleteImage);
  alternationDiv.divElement.append(divForDeleteImage.divElement);
  alternationDiv.divElement.append(divForAlternateImage.divElement);
  taskDiv.append(alternationDiv.divElement);
}

function isMatchedValueForTextArea(value, taskDiv, state) {
  if (value.length > MIN_LENGTH_FOR_TEXT_AREA && value.length < 512) {
    let divAreaElem = new Div("", "hidden", "pop_up_task");
    divAreaElem.renderAppend(taskDiv);
    divAreaElem.divElement.innerText = value;
    setColorByState(divAreaElem.divElement, state)
  }
}

function setColorByState(div, state) {
  switch (state) {
    case "UPCOMING":
      div.style.backgroundColor = "darkgray";
      break;
    case "IN_PROCESS":
      div.style.backgroundColor = "dodgerblue";
      break;
    case "COMPLETED":
      div.style.backgroundColor = "lightgreen"
      break;
    case "FAILED":
      div.style.backgroundColor = "#ffaaaa"
      break;
  }
}
