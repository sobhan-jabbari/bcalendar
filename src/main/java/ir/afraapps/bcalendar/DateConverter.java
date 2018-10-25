package ir.afraapps.bcalendar;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author Ali Jabbari
 */

public final class DateConverter {

  private static final ArrayList<int[]> monthList = new ArrayList<>();
  private static final int[] daysInYear = new int[]{
    355, // 1432
    355, // 1433
    354, // 1434
    355, // 1435
    354, // 1436
    354, // 1437
    354, // 1438
    354, // 1439
    354, // 1440
  };


  static {
    monthList.add(new int[]{0, 30, 30, 29, 30, 30, 30, 29, 30, 29, 29, 30, 29});  // 1432
    monthList.add(new int[]{0, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 30});  // 1433
    monthList.add(new int[]{0, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29});  // 1434
    monthList.add(new int[]{0, 29, 30, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30});  // 1435
    monthList.add(new int[]{0, 29, 30, 29, 29, 30, 29, 30, 29, 30, 29, 30, 30});  // 1436
    monthList.add(new int[]{0, 29, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30});  // 1437
    monthList.add(new int[]{0, 29, 30, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30});  // 1438
    monthList.add(new int[]{0, 29, 30, 30, 30, 30, 29, 30, 29, 29, 30, 29, 29});  // 1439
    monthList.add(new int[]{0, 30, 29, 30, 30, 30, 29, 29, 30, 29, 29, 30, 29});  // 1440
  }

  public static IslamicDate persianToHijriTU(PersianDate persian) {
    if (persian == null || !matchWithYearsLength(persian))
      return null;

    int i = 0;
    int[] months = getMontDayList(i);
    int day = getDaysFromBaseDate(persian); // from 1390-1-1 to intern date
    int month = 4; // month of 1432 in 1390-1-1
    day += 16; // day of 1432 in 1390-1-1
    int year = 1432;

    while (true) {

      if (day > months[month]) {
        day -= months[month];

      } else {
        break;
      }
      month++;

      if (month == 13) {
        year++;
        i++;
        months = getMontDayList(i);
        month = 0;
      }
    }

    return new IslamicDate(year, month, day);
  }

  private static boolean matchWithYearsLength(PersianDate persianDate) {
    return persianDate.getYear() > 1389 && persianDate.getYear() < 1398;
  }

  private static boolean matchWithYearsLength(IslamicDate hijri) {
    return hijri.getYear() > 1431 && hijri.getYear() < 1441;
  }

  public static PersianDate hijriToPersian(IslamicDate hijri) {
    if (hijri == null || !matchWithYearsLength(hijri))
      return null;

    int[] months = new int[]{0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    int day = getHijriDaysFromBaseDate(hijri); // from 1432-1-1 to intern date

    int month = 9; // month of 1389 in 1432-1-1
    day += 16; // day of 1389 in 1432-1-1
    int year = 1389;

    while (true) {

      if (day > months[month]) {
        day -= months[month];
      } else {
        break;
      }
      month++;

      if (month == 13) {
        if (isLipYearInShamsi(year)) {
          day -= 1;
        }
        year++;
        month = 0;
      }
    }

    return new PersianDate(year, month, day);
  }


  private static boolean isLipYearInShamsi(int year) {
    int y;
    if (year > 0)
      y = year - 474;
    else
      y = 473;
    return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
  }


  private static int[] getMontDayList(int i) {
    return monthList.get(i);
  }


  private static int getDaysFromBaseDate(PersianDate persian) {
    if (persian == null)
      return 0;
    int[] monthsList = new int[]{0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
    int baseYear = 1390;
    int year = persian.getYear();
    int month = persian.getMonth();
    int day = persian.getDayOfMonth();

    int yearDiff = year - baseYear;

    int resultOfYearDiff = 0;
    int resultOfMonthsDiff = 0;

    if (yearDiff > 0) {
      for (int j = 0; j < yearDiff; j++) {
        resultOfYearDiff += 365;

        if (isLipYearInShamsi(baseYear)) {
          resultOfYearDiff++;
        }
        baseYear++;
      }
    }

    for (int i = 0; i < month; i++) {
      resultOfMonthsDiff += monthsList[i];
    }

    return resultOfYearDiff + resultOfMonthsDiff + (day - 1);
  }


  private static int getHijriDaysFromBaseDate(IslamicDate hijri) {
    if (hijri == null || !matchWithYearsLength(hijri))
      return 0;

    int yearDiff = hijri.getYear() - 1432;
    int yearDiffDays = 0;
    int month = hijri.getMonth();
    int monthDiffDays = 0;

    //   yearDiffDays = (int) Math.floor(yearDiff * 354.3670834f);

    if (yearDiff > 0) {
      for (int j = 0; j < yearDiff; j++) {
        yearDiffDays += daysInYear[j];
      }
    }

    for (int i = 0; i < month; i++) {
      monthDiffDays += getMontDayList(yearDiff)[i];
    }

    int dayDiffDays = hijri.getDayOfMonth() - 1;

    return yearDiffDays + monthDiffDays + dayDiffDays;
  }


  public static int getDaysToCustomDate(PersianDate date) {
    if (date == null)
      return 0;
    PersianDate now = civilToPersian(new CivilDate());
    int[] monthsList = new int[]{0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
    int baseYear = now.getYear();
    int baseMonth = now.getMonth();
    int baseDay = now.getDayOfMonth();
    int year = date.getYear();
    int month = date.getMonth();
    int day = date.getDayOfMonth();

    int yearDiff = year - baseYear;

    int resultOfYearDiff = 0;
    int resultOfMonthsDiff = 0;
    int resultOfBaseMonthsDiff = 0;

    if (yearDiff > 0) {
      for (int j = 0; j < yearDiff; j++) {
        resultOfYearDiff += 365;

        if (isLipYearInShamsi(baseYear)) {
          resultOfYearDiff++;
        }
        baseYear++;
      }
    }

    for (int i = 0; i < month; i++) {
      resultOfMonthsDiff += monthsList[i];
    }

    for (int i = 0; i < baseMonth; i++) {
      resultOfBaseMonthsDiff += monthsList[i];
    }

    int days1 = resultOfYearDiff + resultOfMonthsDiff + (day - 1);
    int days2 = resultOfBaseMonthsDiff + (baseDay - 1);

    return days1 - days2;
  }


  public static int lipYearsCount(PersianDate persian) {
    int baseYear = 1390;
    int diffYears = persian.getYear() - baseYear;
    int lipYearCount = 0;

    for (int i = 0; i < diffYears; i++) {
      PersianDate date = new PersianDate(persian.getYear() + i, persian.getMonth(), persian.getDayOfMonth());
      if (date.isLeapYear()) {
        lipYearCount++;
      }
    }

    return lipYearCount;

  }


  public static long getRoundDate(long date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 1);

    return calendar.getTimeInMillis();
  }


  public static IslamicDate civilToIslamic(CivilDate civil, int offset) {
    return jdnToIslamic(civilToJdn(civil), offset);
  }


  public static long civilToJdn(CivilDate civil) {
    long year = civil.getYear();
    long month = civil.getMonth();
    long day = civil.getDayOfMonth();

    long jdn = (year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408;
    jdn = jdn - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;
    return jdn;

  }


  public static PersianDate civilToPersian(CivilDate civil) {

    long JDN = civilToJdn(civil);

    int gY = civil.getYear();

    int jY = gY - 621;
    int jM = 1;
    int jD = 1;

    int march = 0;
    int leap = 0;

    int[] breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
      1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};

    gY = jY + 621;
    int leapJ = -14;
    int jp = breaks[0];

    int jump = 0;
    for (int j = 1; j <= 19; j++) {
      int jm = breaks[j];
      jump = jm - jp;
      if (jY < jm) {
        int N = jY - jp;
        leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4;

        if ((jump % 33) == 4 && (jump - N) == 4)
          leapJ = leapJ + 1;

        int leapG = (gY / 4) - (gY / 100 + 1) * 3 / 4 - 150;

        march = 20 + leapJ - leapG;

        if ((jump - N) < 6)
          N = N - jump + (jump + 4) / 33 * 33;

        leap = ((((N + 1) % 33) - 1) % 4);

        if (leap == -1)
          leap = 4;
        break;
      }

      leapJ = leapJ + jump / 33 * 8 + (jump % 33) / 4;
      jp = jm;
    }

    long JDN1F = civilToJdn(new CivilDate(gY, 3, march));
    long k = JDN - JDN1F;
    if (k >= 0) {
      if (k <= 185) {
        jM = (int) (1 + k / 31);
        jD = (int) ((k % 31) + 1);
        return new PersianDate(jY, jM, jD);
      } else {
        k = k - 186;
      }
    } else {
      jY = jY - 1;
      k = k + 179;
      if (leap == 1)
        k = k + 1;
    }

    jM = (int) (7 + k / 30);
    jD = (int) ((k % 30) + 1);

    return new PersianDate(jY, jM, jD);

  }


  public static PersianDate calendarToPersianDate(Calendar calendar) {
    CivilDate civil = new CivilDate(calendar);
    return civilToPersian(civil);
  }


  public static PersianDate calendarToPersianDate(long milliseconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliseconds);
    return calendarToPersianDate(calendar);
  }


  private static long floor(double d) {
    return (long) Math.floor(d);
  }


  public static Calendar civilToCalendar(CivilDate civil) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(civil.getYear(), civil.getMonth() - 1, civil.getDayOfMonth());
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;

  }


  public static Calendar persianToCalendar(PersianDate persian) {
    return civilToCalendar(persianToCivil(persian));

  }


  public static long civilToTimeInMillis(CivilDate civil) {
    Calendar calendar = civilToCalendar(civil);
    return calendar.getTimeInMillis();

  }


  public static CivilDate TimeInMillisToCivil(long milliseconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliseconds);
    return new CivilDate(calendar);

  }


  public static CivilDate islamicToCivil(IslamicDate islamic, int offset) {
    return jdnToCivil(islamicToJdn(islamic, offset));
  }


  public static long islamicToJdn(IslamicDate islamic, int offset) {
    // NMONTH is the number of months between julian day number 1 and
    // the year 1405 A.H. which started immediatly after lunar
    // conjunction number 1048 which occured on September 1984 25d
    // 3h 10m UT.
    int NMONTHS = (1405 * 12 + 1);
    int year = islamic.getYear();
    int month = islamic.getMonth();
    int day = islamic.getDayOfMonth();

    if (year < 0)
      year++;

    long k = month + year * 12 - NMONTHS; // nunber of months since 1/1/1405
    return floor(visibility(k + 1048, offset) + day + 0.5);
  }


  public static PersianDate islamicToPersian(IslamicDate islamic, int offset) {
    return jdnToPersian(islamicToJdn(islamic, offset));
  }


  public static CivilDate jdnToCivil(long jdn) {

    long j = 4 * jdn + 139361631;
    j = j + (((((4 * jdn + 183187720) / 146097) * 3) / 4) * 4 - 3908);
    long i = ((j % 1461) / 4) * 5 + 308;
    int day = (int) ((i % 153) / 5 + 1);
    int month = (int) (((i / 153) % 12) + 1);
    int year = (int) (j / 1461 - 100100 + (8 - month) / 6);

    return new CivilDate(year, month, day);
  }


  public static IslamicDate jdnToIslamic(long jd, int offset) {

    CivilDate civil = jdnToCivil(jd);
    int year = civil.getYear();
    int month = civil.getMonth();
    int day = civil.getDayOfMonth();

    long k = floor(0.6 + (year + (month % 2 == 0 ? month : month - 1) / 12d
      + day / 365f - 1900) * 12.3685);

    double mjd;
    do {
      mjd = visibility(k, offset);
      k = k - 1;
    }
    while (mjd > (jd - 0.5));

    k = k + 1;
    long hm = k - 1048;

    year = 1405 + (int) (hm / 12);
    month = (int) (hm % 12) + 1;

    if (hm != 0 && month <= 0) {
      month = month + 12;
      year = year - 1;
    }

    if (year <= 0)
      year = year - 1;

    day = (int) floor(jd - mjd + 0.5);

    return new IslamicDate(year, month, day);
  }


  // TODO Is it correct to return a CivilDate as a JulianDate?
  public static CivilDate jdnToJulian(long jdn) {

    long j = 4 * jdn + 139361631;
    long i = ((j % 1461) / 4) * 5 + 308;
    int day = (int) ((i % 153) / 5 + 1);
    int month = (int) (((i / 153) % 12) + 1);
    int year = (int) (j / 1461 - 100100 + (8 - month) / 6);

    return new CivilDate(year, month, day);
  }


  public static PersianDate jdnToPersian(long jdn) {

    long i, j;
    int gY, gM;

    int jY, jM, jD;

    j = 4 * jdn + 139361631;

    j = j + (4 * jdn + 183187720) / 146097 * 3 / 4 * 4 - 3908;

    i = (j % 1461) / 4 * 5 + 308;
    gM = (int) (((i / 153) % 12) + 1);
    gY = (int) (j / 1461 - 100100 + (8 - gM) / 6);

    jY = gY - 621;

    int march = 0;
    int leap = 0;

    int[] breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
      1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};

    gY = jY + 621;
    int leapJ = -14;
    int jp = breaks[0];

    int jump = 0;
    for (int ji = 1; ji <= 19; ji++) {
      int jm = breaks[ji];
      jump = jm - jp;
      if (jY < jm) {
        int N = jY - jp;
        leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4;

        if ((jump % 33) == 4 && (jump - N) == 4)
          leapJ = leapJ + 1;

        int leapG = (gY / 4) - (gY / 100 + 1) * 3 / 4 - 150;

        march = 20 + leapJ - leapG;

        if ((jump - N) < 6)
          N = N - jump + (jump + 4) / 33 * 33;

        leap = ((((N + 1) % 33) - 1) % 4);

        if (leap == -1)
          leap = 4;
        break;
      }

      leapJ = leapJ + jump / 33 * 8 + (jump % 33) / 4;
      jp = jm;
    }

    long JDN1F = civilToJdn(new CivilDate(gY, 3, march));
    long k = jdn - JDN1F;
    if (k >= 0) {
      if (k <= 185) {
        jM = (int) (1 + k / 31);
        jD = (int) ((k % 31) + 1);
        return new PersianDate(jY, jM, jD);
      } else {
        k = k - 186;
      }
    } else {
      jY = jY - 1;
      k = k + 179;
      if (leap == 1)
        k = k + 1;
    }

    jM = (int) (7 + k / 30);
    jD = (int) ((k % 30) + 1);

    return new PersianDate(jY, jM, jD);

  }


  public static long julianToJdn(long lYear, long lMonth, long lDay) {
    return (lYear + (lMonth - 8) / 6 + 100100) * 1461 / 4
      + (153 * ((lMonth + 9) % 12) + 2) / 5 + lDay - 34840408;
  }


  public static CivilDate persianToCivil(PersianDate persian) {
    return jdnToCivil(persianToJdn(persian));
  }


  public static IslamicDate persianToIslamic(PersianDate persian, int offset) {
    return jdnToIslamic(persianToJdn(persian), offset);
  }


  public static IslamicDate persianToIslamic(PersianDate persian, boolean isTUG, int offset) {
    return isTUG ? persianToHijriTU(persian) : jdnToIslamic(persianToJdn(persian), offset);
  }


  public static IslamicDate persianToIslamicTU(PersianDate persian) {
    return persianToHijriTU(persian);
  }


  public static long persianToCalendarTime(PersianDate persian) {
    CivilDate civil = persianToCivil(persian);
    Calendar calendar = civilToCalendar(civil);
    return calendar.getTimeInMillis();

  }


  public static long persianToCalendarRound(PersianDate persian) {
    CivilDate civil = persianToCivil(persian);
    Calendar calendar = civilToCalendar(civil);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return getRoundDate(calendar.getTimeInMillis());

  }


  public static long persianToJdn(int year, int month, int day) {
    final long PERSIAN_EPOCH = 1948321; // The JDN of 1 Farvardin 1

    long epbase;
    if (year >= 0)
      epbase = year - 474;
    else
      epbase = year - 473;

    long epyear = 474 + (epbase % 2820);

    long mdays;
    if (month <= 7)
      mdays = (month - 1) * 31;
    else
      mdays = (month - 1) * 30 + 6;

    return day + mdays + ((epyear * 682) - 110) / 2816 + (epyear - 1) * 365
      + epbase / 2820 * 1029983 + (PERSIAN_EPOCH - 1);
  }


  public static long persianToJdn(PersianDate persian) {
    int year = persian.getYear();
    int month = persian.getMonth();
    int day = persian.getDayOfMonth();

    final long PERSIAN_EPOCH = 1948321; // The JDN of 1 Farvardin 1

    long epbase;
    if (year >= 0)
      epbase = year - 474;
    else
      epbase = year - 473;

    long epyear = 474 + (epbase % 2820);

    long mdays;
    if (month <= 7)
      mdays = (month - 1) * 31;
    else
      mdays = (month - 1) * 30 + 6;

    return day + mdays + ((epyear * 682) - 110) / 2816 + (epyear - 1) * 365
      + epbase / 2820 * 1029983 + (PERSIAN_EPOCH - 1);
  }


  private static double tmoonphase(long n, int nph) {

    final double RPD = (1.74532925199433E-02); // radians per degree
    // (pi/180)

    double xtra = 0;

    double k = n + nph / 4d;
    double T = k / 1236.85;
    double t2 = T * T;
    double t3 = t2 * T;
    double jd = 2415020.75933 + 29.53058868 * k - 0.0001178 * t2
      - 0.000000155 * t3 + 0.00033
      * Math.sin(RPD * (166.56 + 132.87 * T - 0.009173 * t2));

    // Sun's mean anomaly
    double sa = RPD
      * (359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3);

    // Moon's mean anomaly
    double ma = RPD
      * (306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3);

    // Moon's argument of latitude
    double tf = RPD
      * 2d
      * (21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3);

    // should reduce to interval 0-1.0 before calculating further
    switch (nph) {
      case 0:
      case 2:
        xtra = (0.1734 - 0.000393 * T) * Math.sin(sa) + 0.0021
          * Math.sin(sa * 2) - 0.4068 * Math.sin(ma) + 0.0161
          * Math.sin(2 * ma) - 0.0004 * Math.sin(3 * ma) + 0.0104
          * Math.sin(tf) - 0.0051 * Math.sin(sa + ma) - 0.0074
          * Math.sin(sa - ma) + 0.0004 * Math.sin(tf + sa) - 0.0004
          * Math.sin(tf - sa) - 0.0006 * Math.sin(tf + ma) + 0.001
          * Math.sin(tf - ma) + 0.0005 * Math.sin(sa + 2 * ma);
        break;
      case 1:
      case 3:
        xtra = (0.1721 - 0.0004 * T) * Math.sin(sa) + 0.0021
          * Math.sin(sa * 2) - 0.628 * Math.sin(ma) + 0.0089
          * Math.sin(2 * ma) - 0.0004 * Math.sin(3 * ma) + 0.0079
          * Math.sin(tf) - 0.0119 * Math.sin(sa + ma) - 0.0047
          * Math.sin(sa - ma) + 0.0003 * Math.sin(tf + sa) - 0.0004
          * Math.sin(tf - sa) - 0.0006 * Math.sin(tf + ma) + 0.0021
          * Math.sin(tf - ma) + 0.0003 * Math.sin(sa + 2 * ma)
          + 0.0004 * Math.sin(sa - 2 * ma) - 0.0003
          * Math.sin(2 * sa + ma);
        if (nph == 1)
          xtra = xtra + 0.0028 - 0.0004 * Math.cos(sa) + 0.0003
            * Math.cos(ma);
        else
          xtra = xtra - 0.0028 + 0.0004 * Math.cos(sa) - 0.0003
            * Math.cos(ma);

        break;
      default:
        return 0;
    }
    // convert from Ephemeris Time (ET) to (approximate)Universal Time (UT)
    return jd + xtra - (0.41 + 1.2053 * T + 0.4992 * t2) / 1440;
  }


  private static double visibility(long n, int offset) {

    // parameters for Makkah: for a new moon to be visible after sunset on
    // a the same day in which it started, it has to have started before
    // (SUNSET-MINAGE)-TIMZ=3 A.M. local time.
    final float TIMZ = 3f, MINAGE = 13.5f, SUNSET = 19.5f, // approximate
      TIMDIF = (SUNSET - MINAGE);

    double jd = tmoonphase(n, 0);
    long d = floor(jd);

    double tf = (jd - d);

    if (tf <= 0.5) // new moon starts in the afternoon
      return (jd + 1f) - offset;
    else { // new moon starts before noon
      tf = (tf - 0.5) * 24 + TIMZ; // local time
      if (tf > TIMDIF)
        return (jd + 1d) - offset; // age at sunset < min for visiblity
      else
        return jd - offset;
    }

  }

}
