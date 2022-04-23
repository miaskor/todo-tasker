const NOW = new Date(),
    MONTH_NAMES = ["January", "February", "March", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"
    ],
    RANGE_VALUE = 8,
    MIN_LENGTH_FOR_TEXT_AREA = 30;

const moveLeft = document.getElementById("move_left"),
    moveRight = document.getElementById("move_right"),
    toDoListDiv = document.getElementById("to_do_list");

let moveableFrom = new Date(),
    moveableTo = new Date(),
    currentFrom = new Date(),
    currentTo = new Date();
currentTo.addDays(RANGE_VALUE / 2);
changeCurrentRange(currentFrom, new Date(currentTo));
changeMoveableRange(currentFrom, currentTo);

const allTasks = {};

//Server

moveLeft.addEventListener("click", () => {
  document.getElementById(currentTo.formatToDMY()).classList.add('hidden');
  const oldToDoDiv = document.getElementById(currentFrom.formatToDMY());
  currentFrom.subtractDays(1);
  currentTo.subtractDays(1);
  const newDate = document.getElementById(currentFrom.formatToDMY());
  if (newDate === null) {
    oldToDoDiv.before(createToDoDay(currentFrom).divElement);
    createTask(currentFrom.formatToDMY());
  } else {
    newDate.classList.remove('hidden');
  }
  if (moveableFrom.getDate() === currentFrom.getDate()) {
    moveableFrom.subtractDays(1);
    const to = new Date(moveableFrom);
    moveableFrom.subtractDays(RANGE_VALUE / 2);
    const from = new Date(moveableFrom);
    getTasks(from.formatToDMY(), to.formatToDMY(), allTasks);
  }
});

moveRight.addEventListener("click", () => {
  document.getElementById(currentFrom.formatToDMY()).classList.add('hidden');
  const oldToDoDiv = document.getElementById(currentTo.formatToDMY());
  currentFrom.addDays(1);
  currentTo.addDays(1);
  const newDate = document.getElementById(currentTo.formatToDMY());
  if (newDate === null) {
    oldToDoDiv.after(createToDoDay(currentTo).divElement);
    createTask(currentTo.formatToDMY());
  } else {
    newDate.classList.remove('hidden');
  }
  if (moveableTo.getDate() === currentTo.getDate()) {
    moveableTo.addDays(1);
    const from = new Date(moveableTo);
    moveableTo.addDays(RANGE_VALUE / 2);
    const to = new Date(moveableTo);
    getTasks(from.formatToDMY(), to.formatToDMY(), allTasks);
  }
});

function dateInRange(startDate, stopDate) {
  const dateArray = [],
      currentDate = new Date(startDate);
  while (currentDate <= stopDate) {
    dateArray.push(new Date(currentDate));
    currentDate.addDays(1);
  }
  return dateArray;
}

function initDateRange(dateFrom, dateTo) {
  const rangeOfDates = dateInRange(dateFrom, dateTo);
  for (let item of rangeOfDates) {
    toDoListDiv.append(createToDoDay(item).divElement)
    createTask(item.formatToDMY());
  }
}

function createToDoDay(date) {
  const divToDoDay = new Div(date.formatToDMY(), 'to_do_day'),
      divTasks = new Div('', 'tasks'),
      divDate = new Div('', 'date'),
      labelDate = document.createElement("label");
  labelDate.textContent = date.formatToDM();
  if (date.formatToDMY() === NOW.formatToDMY()) {
    divToDoDay.divElement.classList.add('current_day');
  }
  divDate.renderAppend(divToDoDay.divElement);
  divDate.divElement.append(labelDate);
  divTasks.renderAppend(divToDoDay.divElement);
  return divToDoDay;
}

const calendar = document.getElementById("date");

function changeMoveableRange(from, to) {
  moveableFrom = new Date(from);
  moveableTo = new Date(to);
  moveableFrom.subtractDays(RANGE_VALUE);
  moveableTo.addDays(RANGE_VALUE);
}

function changeCurrentRange(from, to) {
  currentFrom = new Date(from);
  currentTo = new Date(to);
}

function hideAllToDoDays(all) {
  for (let toDoItem of all) {
    toDoItem.classList.add("hidden");
  }
}

calendar.addEventListener("change", (e) => {
  const from = new Date(e.target.value),
      to = new Date(e.target.value);
  to.addDays(RANGE_VALUE / 2);
  const allToDoDays = toDoListDiv.getElementsByClassName("to_do_day");
  let rangeNewVisibleDates = dateInRange(from, to);
  changeMoveableRange(from, to);
  let rangeMoveableDates = dateInRange(moveableFrom, moveableTo);
  hideAllToDoDays(allToDoDays);
  let tempCursorDateFrom = new Date(moveableFrom);
  let tempCursorDateTo = new Date(moveableTo);
  let isNeedLoadTask = true;
  for (let date of rangeMoveableDates) {
    if (allTasks.hasOwnProperty(tempCursorDateFrom.formatToDMY())) {
      tempCursorDateFrom.addDays(1);
    }
    if (allTasks.hasOwnProperty(tempCursorDateTo.formatToDMY())) {
      tempCursorDateTo.subtractDays(1);
    }
    if (tempCursorDateFrom.formatToDMY()
        === tempCursorDateTo.formatToDMY()) {
      isNeedLoadTask = false;
      break;
    }
  }
  if (isNeedLoadTask) {
    getTasks(tempCursorDateFrom.formatToDMY(),
        tempCursorDateTo.formatToDMY(), allTasks).then(() => {
      generateToDoDays(rangeNewVisibleDates);
    });
  } else {
    generateToDoDays(rangeNewVisibleDates);
  }

  function generateToDoDays(rangeNewVisibleDates) {
    let appendableElem = null;
    for (let date of rangeNewVisibleDates) {
      const existedDate = document.getElementById(date.formatToDMY());
      if (existedDate === null) {
        if (appendableElem === null) {
          appendableElem = createToDoDay(date).divElement;
          let dates = [];
          for (let elem of allToDoDays) {
            dates.push(elem.id);
          }
          const indexDate = binarySearchDate(dates,
              date.formatToDMY());
          if (date > dates[indexDate]) {
            document.getElementById(dates[indexDate]).after(
                appendableElem);
          } else {
            document.getElementById(dates[indexDate]).before(
                appendableElem);
          }
        } else {
          const newElem = createToDoDay(date).divElement
          appendableElem.after(newElem);
          appendableElem = newElem;
        }
        createTask(date.formatToDMY());
      } else {
        appendableElem = existedDate;
        existedDate.classList.remove('hidden');
      }
    }
    changeCurrentRange(rangeNewVisibleDates[0],
        rangeNewVisibleDates[rangeNewVisibleDates.length - 1]);
  }
});

const homeBtn = document.getElementById("home_btn");
homeBtn.addEventListener("click", (e) => {
  e.preventDefault();
  const newFrom = new Date(NOW),
      newTo = new Date(NOW);
  const allToDoDays = toDoListDiv.getElementsByClassName("to_do_day");
  newTo.addDays(RANGE_VALUE / 2);
  changeCurrentRange(newFrom, new Date(newTo));
  changeMoveableRange(newFrom, newTo);
  hideAllToDoDays(allToDoDays);
  for (let date of dateInRange(newFrom, newTo)) {
    document.getElementById(date.formatToDMY()).classList.remove("hidden");
  }
});

getTasks(moveableFrom.formatToDMY(), moveableTo.formatToDMY(), allTasks).then(
    () => {
      initDateRange(currentFrom, currentTo);
    });
