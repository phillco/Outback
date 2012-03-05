package kangaroo.data.convert

/**
 * Converts MeetingTimes + a base into real dates (ie, MWF 8:00AM becomes [10/31 0800, 11/2/11 0800, 11/4/11 0800]).
 */
class ScheduleProjectService {

    /**
     * Given a list of MeetingTimes, returns a list of startTimes and endTimes for this week.
     */
    static def projectToWeek(meetingTimes) {
        def events = []
        meetingTimes.each { meetingTime ->
            meetingTime.daysAsCodes.each { day ->

                // Project the date forward.
                def date = getUpcomingWeekday(dayToOffset(day));
                def startDate = setTime(date, 'hh:mma', meetingTime.startTime);
                def endDate = setTime(date, 'hh:mma', meetingTime.endTime);

                events << [startDate: startDate, endDate: endDate];
            }
        }
        return events;
    }

    /**
     * Given the day of the week (ie, Tuesday), returns the date of that weekday this week (ie, 11/1/11).
     * If the weekday has already occurred this week, it will still be used (the date will be in the past).
     * 0 is Monday, 1 is Tuesday, 2 is Wednesday, etc.
     *
     * @todo making Monday 0 is too arbitrary; use Calendar.XXXDAY values instead.
     */
    static Date getUpcomingWeekday(int dayOfWeek) {
        def date = new Date();

        if (date[Calendar.DAY_OF_WEEK] != Calendar.MONDAY)
            date -= (date[Calendar.DAY_OF_WEEK] - Calendar.MONDAY)

        date += dayOfWeek;
        date;
    }

    /**
     * Sets the given Date's time using the given string and format.
     */
    static Date setTime(Date date, String format, String time) {

        def newDate = new Date().parse(format, time);

        newDate.setYear(date.year)
        newDate.setMonth(date.month)
        newDate.setDate(date.date)
        newDate
    }

    /**
     * Turns a MeetingTime's day code into an offset usable by getUpcomingWeekday().
     * @todo making Monday 0 is too arbitrary; use Calendar.XXXDAY values instead.
     */
    static int dayToOffset(code) {
        def days = ["M", "T", "W", "TH", "F"]
        return days.indexOf(code);
    }
}
