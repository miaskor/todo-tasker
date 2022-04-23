class InputElement {

  #inputElement;

  constructor(typeValue, id, value,
      nameValue, placeHolder, required, ...style) {
    this.typeValue = typeValue;
    this.id = id;
    this.style = style[0] === '' ? '' : style;
    this.value = value;
    this.nameValue = nameValue;
    this.placeHolder = placeHolder;
    this.required = required;
    this.init();
  }

  init() {
    const input = document.createElement('input');
    if (this.typeValue !== '') {
      input.type = this.typeValue;
    }
    if (this.id !== '') {
      input.id = this.id;
    }
    if (this.style !== '') {
      this.style.forEach(item => input.classList.add(item));
    }
    if (this.value !== '') {
      input.value = this.value;
    }
    if (this.nameValue !== '') {
      input.name = this.nameValue;
    }
    if (this.placeHolder !== '') {
      input.placeholder = this.placeHolder;
    }
    if (this.required !== '') {
      input.required = this.required;
    }
    this.#inputElement = input;
  }

  renderAppend(parentElem) {
    parentElem.append(this.#inputElement);
  }

  renderPrepend(parentElem) {
    parentElem.prepend(this.#inputElement);
  }

  get inputElement() {
    return this.#inputElement;
  }
}
