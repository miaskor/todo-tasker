function binarySearchDate(arrDates, date) {
  if (date > arrDates[arrDates.length - 1]) {
    return arrDates.length - 1;
  }
  if (date < arrDates[0]) {
    return 0;
  }
  let begin = 0,
      end = arrDates.length - 1;

  while (begin <= end) {
    let middle = Math.floor((begin + end) / 2);
    let midVal = arrDates[middle];
    if (date > midVal) {
      begin = middle + 1;
    } else if (date < midVal) {
      end = middle - 1;
    } else {
      return Math.floor(middle);
    }
  }
  return Math.floor((begin));
}
