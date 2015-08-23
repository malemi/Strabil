/*
 * <p> <b>jFin, open source derivatives trade processing</b> </p>
 *
 * <p> Copyright (C) 2005, 2006, 2007 Morgan Brown Consultancy Ltd. </p>
 *
 * <p> This file is part of jFin. </p>
 *
 * <p> jFin is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. </p>
 *
 * <p> jFin is distributed in the hope that it will be useful, but <b>WITHOUT
 * ANY WARRANTY</b>; without even the implied warranty of <b>MERCHANTABILITY</b>
 * or <b>FITNESS FOR A PARTICULAR PURPOSE</b>. See the GNU General Public
 * License for more details. </p>
 *
 * <p> You should have received a copy of the GNU General Public License along
 * with jFin; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * St, Fifth Floor, Boston, MA 02110-1301 USA. </p>
 */

package org.strabil.utils;

import java.util.Calendar;

/**
 * Class representing a period between two dates.
 */
public class Period implements Comparable<Period>
{
	private Calendar startCalendar;
	private Calendar endCalendar;


	/**
	 * Default constructor
	 */
	public Period()	{
	}

	/**
	 * Copy constructor
	 * @param toCopy The period to copy
	 */
	public Period(Period toCopy) {
		this.startCalendar = toCopy.startCalendar;
		this.endCalendar = toCopy.endCalendar;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Period clone()
	{
		return new Period(this);
	}



	/**
	 * @param startCalendar
	 *            (Start of period).
	 * @param endCalendar
	 *            (End of period).
	 */
	public Period(Calendar startCalendar, Calendar endCalendar)
	{
		this.startCalendar = startCalendar;
		this.endCalendar = endCalendar;
	}


	/**
	 * @return Returns the startCalendar (Start of period).
	 */
	public Calendar getStartCalendar()
	{
		return startCalendar;
	}

	/**
	 * @param calendar
	 *            The startCalendar to set (Start of period).
	 */
	public void setStartCalendar(Calendar calendar)
	{
		this.startCalendar = calendar;
	}

	/**
	 * @return Returns the endCalendar (End of period).
	 */
	public Calendar getEndCalendar()
	{
		return endCalendar;
	}

	/**
	 * @param calendar
	 *            The endCalendar to set (End of period).
	 */
	public void setEndCalendar(Calendar calendar)
	{
		this.endCalendar = calendar;
	}

	/**
	 * Creates a human readable String representing this period, e.g: 2005/1/1 -
	 * 2006/12/30
	 */
	public String toString()	{
		return ISDADateFormat.format(startCalendar) + " - "
					+ ISDADateFormat.format(endCalendar);
	}

	private long getDoubleMidPoint()
	{
		return startCalendar.getTimeInMillis() + endCalendar.getTimeInMillis();
	}

	/**
	 * Compares the midpoint of this Period with another Period.
	 *
	 * @param arg0 The Period to compare to
	 * @return -1 if arg0 before the midpoint, 0 if arg0 equals or has the same
	 *         midpoint (within an hour), 1 if arg0 after the midpoint
	 */
	public int compareTo(Period arg0)
	{
			Period period = (Period) arg0;

			int retval = (int) ( ( getDoubleMidPoint() - period
					.getDoubleMidPoint() ) / 60000l );

			if (retval == 0)
				return 0;

			return retval / Math.abs(retval);
	}

}
