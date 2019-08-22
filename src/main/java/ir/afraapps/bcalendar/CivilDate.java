package ir.afraapps.bcalendar;

import java.util.Calendar;
import java.util.Locale;


/**
 * @author Ali Jabbari
 */
public class CivilDate extends AbstractDate {

  private static final String[] monthName = {"", "January", "February", "March",
    "April", "May", "June", "July", "August", "September", "October",
    "November", "December"};

  private static final String[] monthPersianName = {"", "ژانویه", "فوریه", "مارس",
    "آوریل", "مه", "ژوئن", "ژوئیه", "اوت", "سپتامبر", "اکتبر",
    "نوامبر", "دسامبر"};

  private static final int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31,
    31, 30, 31, 30, 31};
  private int year;
  private int month;
  private int day;

  public CivilDate() {
    this(Calendar.getInstance());
  }


  public CivilDate(Calendar calendar) {
    this.year = calendar.get(Calendar.YEAR);
    this.month = calendar.get(Calendar.MONTH) + 1;
    this.day = calendar.get(Calendar.DAY_OF_MONTH);
  }


  public CivilDate(int year, int month, int day) {
    this();
    setYear(year);
    setMonth(month);
    setDayOfMonth(day);
  }

  @Override
  public String[] getMonthsList() {
    return monthName;
  }

  @Override
  public int getDayOfMonth() {
    return day;
  }

  @Override
  public void setDayOfMonth(int day) {
    if (day < 1)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (month != 2 && day > daysInMonth[month])
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (month == 2 && isLeapYear() && day > 29)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (month == 2 && (!isLeapYear()) && day > 28)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    // TODO check for the case of leap year for February
    this.day = day;
  }

  public int getDaysInMonth(int month) {
    return daysInMonth[month];
  }


  @Override
  public int getDayOfWeek() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, day);
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  @Override
  public int getDayOfYear() {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public String getEvent() {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public int getMonth() {
    return month;
  }

  @Override
  public void setMonth(int month) {
    if (month < 1 || month > 12)
      throw new MonthOutOfRangeException("month " + month
        + " is out of range!");

    // Set the day again, so that exceptions are thrown if the
    // day is out of range
    setDayOfMonth(getDayOfMonth());

    this.month = month;
  }

  @Override
  public String getMonthName() {
    return monthName[getMonth()];
  }


  public String getMonthPersianName() {
    return monthPersianName[getMonth()];
  }

  @Override
  public int getWeekOfMonth() {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public int getWeekOfYear() {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public int getYear() {
    return year;
  }

  @Override
  public void setYear(int year) {
    if (year == 0)
      throw new YearOutOfRangeException("Year 0 is invalid!");

    this.year = year;
  }

  @Override
  public boolean isLeapYear() {
    if (year % 400 == 0)
      return true;
    else if (year % 100 == 0)
      return false;
    else if (year % 4 == 0)
      return true;
    else
      return false;

  }

  @Override
  public void rollDay(int amount, boolean up) {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public void rollMonth(int amount, boolean up) {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public void rollYear(int amount, boolean up) {
    throw new RuntimeException("not implemented yet!");
  }

  @Override
  public String getDayOfWeekName() {
    return weekdayName[getDayOfWeek()];
  }

  @Override
  public String getDayOfWeekNameShort() {
    return weekdayShortName[getDayOfWeek()];
  }


  public boolean isToday() {
    CivilDate now = new CivilDate();
    return now.getYear() == getYear()
      && now.getMonth() == getMonth()
      && now.getDayOfMonth() == getDayOfMonth();
  }


  @Override
  public CivilDate clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      //
    }
    return new CivilDate(this.getYear(), this.getMonth(), this.getDayOfMonth());
  }


  @Override
  public boolean equals(Object obj) {
    return obj instanceof CivilDate
      && this.getDayOfMonth() == ((CivilDate) obj).getDayOfMonth()
      && this.getMonth() == ((CivilDate) obj).getMonth()
      && this.getYear() == ((CivilDate) obj).getYear();
  }


  @Override
  public String toString() {
    return String.format(Locale.ENGLISH, "%s %s %s",
      getDayOfMonth(),
      getMonthPersianName(),
      getYear());
  }


  @Override
  public String toStringShort() {
    return String.format(Locale.ENGLISH, "%s %s",
      getDayOfMonth(),
      getMonthPersianName());
  }

  @Override
  public String toStringMonthYeay() {
    return String.format(Locale.ENGLISH, "%s %s",
      getYear(),
      getMonthPersianName());
  }

  @Override
  public String toStringByWeekName() {
    return String.format(Locale.ENGLISH, "%s %s %s %s",
      getDayOfWeekName(),
      getDayOfMonth(),
      getMonthPersianName(),
      getYear());
  }
}
