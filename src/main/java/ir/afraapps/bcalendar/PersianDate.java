package ir.afraapps.bcalendar;

import java.util.Calendar;


/**
 * @author Ali Jabbari
 */
public class PersianDate extends AbstractDate {

  private static final String[] persianMonthName = {"", "فروردین",
    "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
    "آذر", "دی", "بهمن", "اسفند"};

  private static final String today = "امروز";

  private static final int[] daysOfMonth = {0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
  private int year;
  private int month;
  private int day;

  public PersianDate() {
    PersianDate persianDate = DateConverter.civilToPersian(new CivilDate());
    setDayOfMonth(persianDate.getDayOfMonth());
    setMonth(persianDate.getMonth());
    setYear(persianDate.getYear());
  }

  public PersianDate(CivilDate civilDate) {
    PersianDate persianDate = DateConverter.civilToPersian(civilDate);
    setDayOfMonth(persianDate.getDayOfMonth());
    setMonth(persianDate.getMonth());
    setYear(persianDate.getYear());
  }

  public PersianDate(int year, int month, int day) {
    setYear(year);
    // Initialize day, so that we get no exceptions when setting month
    this.day = 1;
    setMonth(month);
    setDayOfMonth(day);
  }

  @Override
  public String[] getMonthsList() {
    return persianMonthName;
  }


  @Override
  public void setDate(int year, int month, int day) {
    setYear(year);
    this.day = 1;
    setMonth(month);
    setDayOfMonth(day);
  }

  @Override
  public PersianDate clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      //
    }
    return new PersianDate(getYear(), getMonth(), getDayOfMonth());
  }


  @Override
  public int getDayOfMonth() {
    return day;
  }

  @Override
  public void setDayOfMonth(int day) {
    if (day < 1)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (month <= 6 && day > 31)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (month > 6 && month <= 12 && day > 30)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if (isLeapYear() && month == 12 && day > 30)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    if ((!isLeapYear()) && month == 12 && day > 29)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    this.day = day;
  }

  public int getDaysInMonth(int year, int month) {
    setYear(year);
    setMonth(month);
    setDayOfMonth(1);
    if (isLeapYear() && month == 12) {
      return 30;
    } else {
      return daysOfMonth[month];
    }
  }

  public int getDaysInMonth() {
    if (month == 12 && isLeapYear()) {
      return 30;
    } else {
      return daysOfMonth[month];
    }
  }

  public int getDaysInYear() {
    if (isLeapYear()) {
      return 366;
    } else {
      return 365;
    }
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
    setDayOfMonth(day);

    this.month = month;
  }

  @Override
  public String getMonthName() {
    return getMonthsList()[month];
  }


  public static String getMonthName(int month) {
    return persianMonthName[month];
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


  public static String getDayOfWeekName(int day) {
    return weekdayName[day];
  }


  public static String getDayOfWeekNameShort(int day) {
    return weekdayShortName[day];
  }


  @Override
  public String getEvent() {
    throw new RuntimeException("not implemented yet!");
  }


  @Override
  public int getDayOfWeek() {
    PersianDate persian = clone();
    return DateConverter.persianToCalendar(persian).get(Calendar.DAY_OF_WEEK);
  }


  @Override
  public int getDayOfYear() {
    throw new RuntimeException("not implemented yet!");
  }


  @Override
  public int getWeekOfMonth() {
    PersianDate persian = clone();
    persian.setDayOfMonth(1);
    int dayOfWeek = persian.getDayOfWeek() % 7;
    return 1 + ((day + dayOfWeek) / 7);
  }


  public int getWeeksOfMonth() {
    PersianDate persian = clone();
    persian.setDayOfMonth(1);
    int dayOfWeek = persian.getDayOfWeek() % 7;
    return 1 + ((getDaysInMonth() + dayOfWeek) / 7);
  }


  public PersianDate addMonth(int offset) {
    int month = getMonth() - offset;
    month -= 1;

    int year = getYear();

    year = year + (month / 12);
    month = month % 12;
    if (month < 0) {
      year -= 1;
      month += 12;
    }
    month += 1;
    setMonth(month);
    setYear(year);
    setDayOfMonth(1);
    return this;
  }


  @Override
  public boolean isLeapYear() {
    int y;
    if (year > 0)
      y = year - 474;
    else
      y = 473;
    return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
  }


  public boolean isLeapYear(PersianDate persian) {
    int y;
    if (persian.getYear() > 0)
      y = persian.getYear() - 474;
    else
      y = 473;
    return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
  }


  public long getTimeInMillis() {
    return DateConverter.persianToCalendar(this.clone()).getTimeInMillis();
  }


  public String getMonthYearTitle() {
    return getMonthName() + ' ' + getYear();
  }


  public boolean isToday() {
    PersianDate now = new PersianDate();
    return now.getYear() == getYear()
      && now.getMonth() == getMonth()
      && now.getDayOfMonth() == getDayOfMonth();
  }


  @Override
  public boolean equals(Object obj) {
    return obj instanceof PersianDate
      && this.getDayOfMonth() == ((PersianDate) obj).getDayOfMonth()
      && this.getMonth() == ((PersianDate) obj).getMonth()
      && this.getYear() == ((PersianDate) obj).getYear();
  }


  public String toStringFull() {
    StringBuilder sb = new StringBuilder();
    if (isToday()) {
      sb.append(today).append(": ");
    }

    sb.append(toStringByWeekName());

    return sb.toString();
  }


}
