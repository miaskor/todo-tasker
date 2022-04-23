"use strict"

const headerSingIn = document.getElementById("signIn"),
    headerSignUp = document.getElementById("signUp"),
    form = document.getElementById("form"),
    email = document.getElementById("email"),
    login = document.getElementById("login"),
    password = document.getElementById("password"),
    submitButton = document.getElementById("sbm_btn"),
    error = document.getElementById("error");

//Change headers sides (swap headers)

function showEmail() {
  form.setAttribute('action', '/registration');
  email.classList.remove('hidden');
  email.required = true;
  submitButton.value = 'Sign up';
}

function hideEmail() {
  form.setAttribute('action', '/auth');
  email.classList.add('hidden');
  email.required = false;
  submitButton.value = 'Sign in';
}

function changeHeaderActiveStatusClass(listActive, listNonActive) {
  listNonActive.add('active');
  listNonActive.remove('inactive', 'underlineHover');
  listActive.add('inactive', 'underlineHover');
  listActive.remove('active');
}

function changeActiveHeader() {
  headerSingIn.addEventListener('click', () => {
    changeActiveStatus(headerSignUp, headerSingIn);
    form.reset();
  });

  headerSignUp.addEventListener('click', () => {
    changeActiveStatus(headerSingIn, headerSignUp);
    form.reset();
  });

  function changeActiveStatus(activeElement, nonActiveElement) {
    if (nonActiveElement.id === 'signUp') {
      showEmail();
    } else {
      hideEmail();
    }
    changeHeaderActiveStatusClass(activeElement.classList,
        nonActiveElement.classList);
  }
}

changeActiveHeader();

//Server

function setErrorData(data) {
  if (data.message.includes("password")) {
    login.style.backgroundColor = "#772255"
    login.style.outline = "#333333"
    login.style.border = "1px"
    password.style.backgroundColor = "#772255"
    password.style.outline = "#333333"
    password.style.border = "1px"
    error.textContent = `Login or password is invalid`;
  } else if (data.message.includes("email")) {
    email.style.backgroundColor = "#772255"
    email.style.outline = "#333333"
    email.style.border = "1px"
    error.textContent = `Email is invalid`;
  } else if (data.message.includes("login")) {
    login.style.backgroundColor = "#772255"
    login.style.outline = "#333333"
    login.style.border = "1px"
    error.textContent = `Login is invalid`;
  } else {
    error.textContent = `Something went wrong!`
  }
  error.classList.remove("hidden");
}

form.addEventListener('submit', (e) => {
  e.preventDefault();
  const url = form.getAttribute('action');
  const data = {};
  new FormData(form).forEach((item, key) => {
    data[key] = item;
  });
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-type': 'application/json'
    },
    body: JSON.stringify(data)
  }).then(response => {
    if (response.ok) {
      if (response.redirected) {
        document.location = response.url;
      }
    } else {
      response.json().then(body => {
        setErrorData(body)
      })
    }
  })
});

document.addEventListener('click', () => {
  isParagraphHidden(error);

  //isParagraphHidden(errorParagraphToCheckEmail);
  function isParagraphHidden(element) {
    if (!element.classList.contains('hidden')) {
      element.classList.add('hidden');
      element.textContent = '';
      login.style = '';
      password.style = '';
      email.style = '';
    }
  }
});
