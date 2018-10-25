package ir.afraapps.bcalendar;

import java.util.Calendar;


/**
 * @author Ali Jabbari
 */
public class IslamicDate extends AbstractDate {

  public static final int TUG = 0;
  public static final int PREVALENT = 1;

  private static final String[] monthName = {"", "محرم", "صفر",
    "ربيع‌الاول", "ربيع‌الثاني", "جمادي‌الاول", "جمادي‌الثاني", "رجب",
    "شعبان", "رمضان", "شوال", "ذي‌القعده", "ذي‌الحجه"};


  @Override
  public String[] getMonthsList() {
    return monthName;
  }

  private int day;
  private int month;
  private int year;
  private int method;
  private int offset;


  public IslamicDate(int offset) {
    setMethod(PREVALENT);
    setOffset(offset);
    PersianDate persianDate = new PersianDate();
    IslamicDate islamicDate = DateConverter.persianToIslamic(persianDate, offset);
    setDayOfMonth(islamicDate.getDayOfMonth());
    setMonth(islamicDate.getMonth());
    setYear(islamicDate.getYear());
  }

  public IslamicDate() {
    setMethod(TUG);
    PersianDate persianDate = new PersianDate();
    IslamicDate islamicDate = DateConverter.persianToHijriTU(persianDate);
    if (islamicDate != null) {
      setDayOfMonth(islamicDate.getDayOfMonth());
      setMonth(islamicDate.getMonth());
      setYear(islamicDate.getYear());
    }
  }

  public IslamicDate(int year, int month, int day) {
    setYear(year);
    // Initialize day, so that we get no exceptions when setting month
    this.day = 1;
    setMonth(month);
    setDayOfMonth(day);
  }


  @Override
  public int getDayOfMonth() {
    return day;
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
  public int getMonth() {
    return month;
  }


  @Override
  public String getMonthName() {
    return monthName[month];
  }


  public static String getMonthName(int month) {
    return monthName[month];
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


  @Override
  public void setDayOfMonth(int day) {
    // TODO This check is not very exact! But it's not worth of it
    // to compute the number of days in this month exactly
    if (day < 1 || day > 30)
      throw new DayOutOfRangeException("day " + day + " is out of range!");

    this.day = day;
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
  public void setYear(int year) {
    if (year == 0)
      throw new YearOutOfRangeException("Year 0 is invalid!");

    this.year = year;
  }


  @Override
  public String getEvent() {
    throw new RuntimeException("not implemented yet!");
  }


  @Override
  public int getDayOfYear() {
    throw new RuntimeException("not implemented yet!");
  }


  @Override
  public int getWeekOfMonth() {
    throw new RuntimeException("not implemented yet!");
  }


  @Override
  public boolean isLeapYear() {
    throw new RuntimeException("not implemented yet!");
  }


  public int getDaysOfMonth() {
    IslamicDate islamic = clone();
    islamic.setDayOfMonth(29);

    PersianDate persianDate1 = isTUG()
      ? DateConverter.hijriToPersian(islamic)
      : DateConverter.islamicToPersian(islamic, offset);

    Calendar calendar = DateConverter.persianToCalendar(persianDate1);
    calendar.add(Calendar.DAY_OF_MONTH, 1);

    PersianDate persianDate2 = DateConverter.calendarToPersianDate(calendar);

    IslamicDate hijri = isTUG()
      ? DateConverter.persianToHijriTU(persianDate2)
      : DateConverter.persianToIslamic(persianDate2, offset);

    int day = hijri.getDayOfMonth();

    return Math.max(day, 29);
  }


  public int getMethod() {
    return method;
  }


  public void setMethod(int method) {
    this.method = method;
  }


  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public boolean isTUG() {
    return method == TUG;
  }


  public boolean isToday() {
    IslamicDate now = isTUG() ? new IslamicDate() : new IslamicDate(offset);
    return now.getYear() == getYear()
      && now.getMonth() == getMonth()
      && now.getDayOfMonth() == getDayOfMonth();
  }


  @Override
  public IslamicDate clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      //
    }
    return new IslamicDate(getYear(), getMonth(), getDayOfMonth());
  }


  public boolean equals(IslamicDate islamicDate) {
    if (this.getDayOfMonth() == islamicDate.getDayOfMonth()
      && this.getMonth() == islamicDate.getMonth()
      && this.getYear() == islamicDate.getYear())
      return true;
    return false;
  }


  @Override
  public boolean equals(Object obj) {
    return obj instanceof IslamicDate &&
      getYear() == ((IslamicDate) obj).getYear() &&
      getMonth() == ((IslamicDate) obj).getMonth() &&
      getDayOfMonth() == ((IslamicDate) obj).getDayOfMonth();
  }


}
