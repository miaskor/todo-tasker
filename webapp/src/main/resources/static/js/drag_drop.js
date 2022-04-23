function dragStart() {
  setTimeout(() => (this.className = 'invisible'), 0);
}

function dragOver(e) {
  e.preventDefault();
}

function dragEnd(e) {
  e.preventDefault();
}

function dragEnter(e) {
  e.preventDefault();
}

function dragLeave(e) {
  e.preventDefault();
}

function dragDrop(element) {
  this.append(element);
}

function addDroppable(element) {
  element.ondragover = dragOver;
  element.ondragenter = dragEnter;
  element.ondragleave = dragLeave;
  element.ondrop = dragDrop;
}

function addDraggable(element) {
  element.ondragstart = dragStart;
  element.ondragend = dragEnd;
}
