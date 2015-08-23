/*
 Copyright (C) 2009 Ueli Hofstetter

 This source code is release under the BSD License. 

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/
 */

/**
 * 
 * Originally part of JQuantLib. Simplified, currency s now just a String in order to ensure that 
 * the application developer is using the same currency everywhere.
 * 
 * @author Ueli Hofstetter
 * 
 */


package org.strabil.currencies;
import java.io.Serializable;

import org.strabil.utils.LibraryException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Cash amount in a given currency.
 */
@XStreamAlias("money")
public class Money implements Serializable {
    /**
	 * For serialization
	 */
	private static final long serialVersionUID = -4820155970811354721L;
	public static String baseCurrency;

    // enums
    public enum ConversionType {
        NoConversion, /*   do not perform conversions */
        BaseCurrencyConversion, /*
                                 *   convert both operands to the base currency before converting
                                 */
        AutomatedConversion
        /*
         *  return the result in the currency of the first operand
         */
    };

    // class fields
    private double value;
    
   	@XStreamAsAttribute
    private String currency;

    // constructors
    public Money() {
        this.value = (0.0);
    }
	
    public Money(final String currency,  final double value) {
        this.value = (value);
        this.currency = (currency);
    }

    public Money( final double value, final String currency) {
        this.value = (value);
        this.currency = currency;
    }

    @Override
    public Money clone() {
        final Money money = new Money();
        money.currency = currency;
        money.value = value;
        return money;
    }

    // accessors
    public String getCurrency() {
        return currency;
    }

    public double getValue() {
        return value;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Money rounded() {
//TODO
    	return this;
    }


    // class based operators

    // +() //FIXME: this looks like a mistake in c++
    public Money positiveValue() {
        return new Money(currency, value);
    }

    // -()
    public Money negativeValue() {
        return new Money(-value, currency);
    }

    // *=
    public Money mulAssign( final double x) {
        value *= x;
        return this;
    }


    // +=
    public Money addAssign( final Money money) {
    	if(!this.currency.equalsIgnoreCase(money.getCurrency()) ) 
    		throw new LibraryException("Trying to add Money with different currency.");
    	this.value+= money.getValue();
    	return this;
    }

    // /=
    public Money divAssign( final double x) {
        value /= x;
        return this;
    }

    // static operators

    // +
    public Money add(final Money money) {
    	if(!this.currency.equalsIgnoreCase(money.getCurrency()) ) 
    		throw new LibraryException("Trying to add Money with different currency.");
    	Money tmp = new Money(this.currency, this.value+money.getValue());
        return tmp;
    }

    // -
    public Money sub(final Money money) {
    	if(!this.currency.equalsIgnoreCase(money.getCurrency()) ) 
    		throw new LibraryException("Trying to add Money with different currency.");
        Money tmp = new Money(this.currency, this.value-money.getValue());
        return tmp;
    }

    // *
    public Money mul( final double x) {
        final Money tmp = clone();
        tmp.mulAssign(x);
        return tmp;
    }

    public Money div( final double x) {
        final Money tmp = clone();
        tmp.value /= x;
        return tmp;
    }

    public Money div( final Money money) {
    	if(!this.currency.equalsIgnoreCase(money.getCurrency()) ) 
    		throw new LibraryException("Trying to add Money with different currency.");
        Money tmp = new Money(this.currency, this.value / money.getValue());
        return tmp;
    }

    public boolean notEquals(final Money money) {
        // eating dogfood
        return !(this.equals(money));
    }

    public boolean greater(final Money money) {
        return money.greater(this);

    }

    public boolean greaterEqual(final Money money) {
        return money.greaterEqual(this);
    }

    public static Money multiple(final String c,  final double value) {
        return new Money(value, c);
    }

    public static Money multiple(  final double value, final String c) {
        return new Money(value, c);
    }


}
