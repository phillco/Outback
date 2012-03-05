package kangaroo.data.convert

import kangaroo.MeetingTime

/**
 * Converts the "MWF 09:00AM 10:00AM" schedule format to (and from) MeetingTimes.
 */
class ScheduleConvertService {

    /**
     * Converts the given "MWF 09:00AM 10:00AM" string to a MeetingTime. The MeetingTime is NOT yet persisted.
     * Returns null if the MeetingTime could not be parsed.
     */
    static def MeetingTime convertMeetingTime(String composite) {

        try {
            if (composite.contains("TBA"))
                return null;

            // Make sure that there's a space after the day codes. (WebHopper won't have one if they are all on)
            if (composite.startsWith("MTWTHF"))
                composite = composite.replaceAll("MTWTHF", "MTWTHF ")

            // Split the string into three parts ("MWF", "09:00AM", "10:00AM") without padding.
            def parts = (List<String>) composite.split(" ").findAll { it.length() > 0 }

            // Set the meeting time's properties and return.
            return setDayCodes(new MeetingTime(startTime: parts[1], endTime: parts[2]), parts[0]);
        }
        catch (Exception e) {
            println "Error parsing schedule $composite ($e)..."
        }
    }

    /**
     * Sets the meeting time's "meetsXDay" properties to match the "MWF" codes in the string.
     */
    static MeetingTime setDayCodes(MeetingTime meetingTime, String days) {
        meetingTime.with {

            // Check "TH" first and remove it so "T" does not match.
            if (days.contains("TH"))
                meetsThursday = true;
            days = days.replaceAll("TH", "")

            // Check for the rest
            if (days.contains("M"))
                meetsMonday = true;
            if (days.contains("T"))
                meetsTuesday = true;
            if (days.contains("W"))
                meetsWednesday = true;
            if (days.contains("F"))
                meetsFriday = true;
        }

        return meetingTime
    }

    /**
     * Given a MeetingTime, returns a list of its day codes (["M", "W", "TH"]).
     */
    static List<String> getDaysAsCodes(MeetingTime meetingTime) {
        List<String> days = [];
        meetingTime.with {
            if (meetsMonday)
                days << "M";
            if (meetsTuesday)
                days << "T";
            if (meetsWednesday)
                days << "W";
            if (meetsThursday)
                days << "TH";
            if (meetsFriday)
                days << "F";
        }
        return days;
    }

    /**
     * Given a MeetingTime, returns a list of the days it meets (["Monday", "Wednesday", "Thursday"]).
     */
    static List<String> getDaysAsWords(MeetingTime meetingTime) {
        final codesToWords = ["M": "Monday", "T": "Tuesday", "W": "Wednesday", "TH": "Thursday", "F": "Friday"]

        // A simple lookup table operation.
        return meetingTime.daysAsCodes.collect { code -> codesToWords[code] }
    }
}
