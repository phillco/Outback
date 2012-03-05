package kangaroo

import kangaroo.data.convert.ScheduleConvertService
import kangaroo.mn.CourseMeetingTime

/**
 * Represents a recurring meeting schedule as used by courses or professors ("MWF 09:00AM 10:00AM").
 * Each MeetingTime instance supports a single time interval (ie, 3pm to 5pm) that occurs on several days (ie, Tuesdays and Thursdays).
 *
 * @todo [PC] I'd like to find a better way to represent this data, but haven't seen a model I'm happy with yet.
 */
class MeetingTime {

    static transients = ['daysAsCodes', 'daysAsWords', 'daysAsString']

    // Indicated if we meet on any of these days.
    boolean meetsMonday, meetsTuesday, meetsWednesday, meetsThursday, meetsFriday;

    // The time we start meeting and the time we stop.
    String startTime, endTime

    static constraints = {
        // Force the start and stop time into the appropriate format.
        startTime(maxSize: 7, matches: /\d{2}:\d{2}[AP]M/)
        endTime(maxSize: 7, matches: /\d{2}:\d{2}[AP]M/)
    }

    /**
     * Returns the courses meeting at this time.
     */
    List<Course> getCoursesMeeting() { CourseMeetingTime.findAllByMeetingTime(this)*.course }

    /**
     * Returns a list of all of the days we meet, as codes (["M", "W", "TH"]).
     */
    List<String> getDaysAsCodes() { ScheduleConvertService.getDaysAsCodes(this) }

    /**
     * Returns a list of all of the days we meet, as words (["Monday", "Wednesday", "Thursday"]).
     */
    List<String> getDaysAsWords() { ScheduleConvertService.getDaysAsWords(this) }

    /**
     * Returns a list of all of the days we meet, as a flat string ("MWTH").
     */
    String getDaysAsString() { getDaysAsCodes().join("") }

    /**
     * Forces this MeetingTime to the database; either commits it new or finds an existing exact match.
     */
    def MeetingTime saveOrFind() {

        // See if this exact meeting time already exists.
        if (MeetingTime.find(this))
            return MeetingTime.find(this);
        else
            return save();
    }

    boolean equals(Object other) { toString() == other.toString() }

    String toString() { "$daysAsString $startTime $endTime" }

}
