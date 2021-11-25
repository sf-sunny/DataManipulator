package ui;

import model.EventLog;

/**
 * Defines behaviours that event log printers must support.
 */
//reference: CPSC 210 AlarmSystem https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
public interface LogPrinter {
	/**
	 * Prints the log
	 * @param el  the event log to be printed
	 */
    void printLog(EventLog el);
}
