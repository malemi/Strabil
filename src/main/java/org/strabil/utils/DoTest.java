package org.strabil.utils;

// TODO: OSGi :: remove statics
public class DoTest {

    /**
     * Throws an error if a <b>pre-condition</b> is not verified
     * <p>
     * @note  this method should <b>never</b> be removed from bytecode by AspectJ.
     *        If you do so, you must be plenty sure of effects and risks of this decision.
     * <p>
     * @param condition is a condition to be verified
     * @param message is a message emitted.
     * @throws LibraryException if the condition is not met
     */
	
    public static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new LibraryException(message);
        }
    }

    /**
     * Throws an error if a <b>post-condition</b> is not verified
     * <p>
     * @note  this method should <b>never</b> be removed from bytecode by AspectJ.
     *        If you do so, you must be plenty sure of effects and risks of this decision.
     * <p>
     * @param condition is a condition to be verified
     * @param message is a message emitted.
     * @throws LibraryException if the condition is not met
     */
    public static void ensure(final boolean condition, final String message) {
        if (!condition) {
            throw new LibraryException(message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void error(final String message) {
        if (SLogger.logger!=null) {
            SLogger.logger.error(message);
        } else {
            System.err.printf("ERROR: %s\n", message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void error(final String message, final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.error(message, t);
        } else {
            System.err.printf("ERROR: %s : %s\n", message, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     */
    public static void error(final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.error(t.getMessage(), t);
        } else {
            System.err.printf("ERROR: %s\n", t.getMessage());
            System.err.println(t.getMessage());
            t.printStackTrace(System.err);
        }
    }




    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void warn(final String message) {
        if (SLogger.logger!=null) {
            SLogger.logger.warn(message);
        } else {
            System.err.printf("WARN: %s\n", message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void warn(final String message, final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.warn(message, t);
        } else {
            System.err.printf("WARN: %s : %s\n", message, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     */
    public static void warn(final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.warn(t.getMessage(), t);
        } else {
            System.err.printf("WARN: %s\n", t.getMessage());
            System.err.println(t.getMessage());
            t.printStackTrace(System.err);
        }
    }




    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void info(final String message) {
        if (SLogger.logger!=null) {
            SLogger.logger.info(message);
        } else {
            System.out.printf("%s\n", message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void info(final String message, final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.info(message, t);
        } else {
            System.err.printf("INFO: %s : %s\n", message, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     */
    public static void info(final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.info(t.getMessage(), t);
        } else {
            System.err.printf("INFO: %s\n", t.getMessage());
            System.err.println(t.getMessage());
            t.printStackTrace(System.err);
        }
    }




    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void debug(final String message) {
        if (SLogger.logger!=null) {
            SLogger.logger.debug(message);
        } else {
            System.err.printf("DEBUG: %s\n", message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void debug(final String message, final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.debug(message, t);
        } else {
            System.err.printf("DEBUG: %s : %s\n", message, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     */
    public static void debug(final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.debug(t.getMessage(), t);
        } else {
            System.err.printf("DEBUG: %s\n", t.getMessage());
            System.err.println(t.getMessage());
            t.printStackTrace(System.err);
        }
    }




    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void trace(final String message) {
        if (SLogger.logger!=null) {
            SLogger.logger.trace(message);
        } else {
            System.err.printf("TRACE: %s\n", message);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     *
     * @param message is a message to be emitted
     */
    public static void trace(final String message, final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.trace(message, t);
        } else {
            System.err.printf("TRACE: %s : %s\n", message, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    /**
     * This method unconditionally emits a message to the logging system but does not throw any exception.
     */
    public static void trace(final Throwable t) {
        if (SLogger.logger!=null) {
            SLogger.logger.trace(t.getMessage(), t);
        } else {
            System.err.printf("TRACE: %s\n", t.getMessage());
            System.err.println(t.getMessage());
            t.printStackTrace(System.err);
        }
    }
    
    /**
     * This method to validate whether code is being run in
     * experimental mode or not
     */
    public static void validateExperimentalMode(){
    	if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    
    
}
