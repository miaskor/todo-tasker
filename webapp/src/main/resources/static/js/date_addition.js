Date.prototype.addDays = function (days) {
  this.setDate(this.getDate() + days);
}

Date.prototype.subtractDays = function (days) {
  this.setDate(this.getDate() - days);
}

Date.prototype.formatToDM = function () {
  return this.getDate() + ' ' + MONTH_NAMES[this.getMonth()];
}

Date.prototype.formatToDMY = function () {
  return this.getFullYear() + '-' + isNeededZero(this.getMonth() + 1) + '-'
      + isNeededZero(this.getDate());
}

function isNeededZero(date) {
  if (date > 0 && date < 10) {
    return '0' + date;
  }
  return date;
}
