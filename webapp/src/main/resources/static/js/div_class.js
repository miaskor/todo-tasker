class Div {

  #divElement;

  constructor(id, ...style) {
    this.id = id;
    this.style = style === '' ? '' : style;
    this.init();
  }

  init() {
    const div = document.createElement('div');
    if (this.id !== '') {
      div.id = this.id;
    }
    if (this.style !== '') {
      this.style.forEach(item => div.classList.add(item));
    }
    this.#divElement = div;
  }

  renderAppend(parentElem) {
    parentElem.append(this.#divElement);
  }

  renderPrepend(parentElem) {
    parentElem.prepend(this.#divElement);
  }

  get divElement() {
    return this.#divElement;
  }
}
