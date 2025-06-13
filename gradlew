#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname "$PRG"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if $cygwin ; then
    [ -n "$APP_HOME" ] &&
        APP_HOME=`cygpath --unix "$APP_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$CLASSPATH" ] &&
        CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# Ensure that neither JAVA_HOME nor CLASSPATH are set (unless you know what you're doing)
if [ -n "$JAVA_HOME" ] && [ ! -d "$JAVA_HOME" ]; then
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi


# Attempt to find java
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Set  CLASSPATH
# Will be created by the wrapper task, see https://docs.gradle.org/current/userguide/gradle_wrapper.html
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"


# Determine the Java command to use to start the JVM.
JAVACMD_LAUNCHER="$JAVACMD"


# Increase the maximum file descriptors if we can.
if ! $cygwin && ! $darwin && ! $nonstop ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            # use the system max
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Darwin, add options to specify how the application appears in the dock;
# also
# prevent unwanted focus stealing on OS X
# See https://docs.gradle.org/current/userguide/gradle_wrapper.html#configuring_the_wrapper
if $darwin; then
    GRADLE_OPTS="$GRADLE_OPTS "-Xdock:name=$APP_NAME" "-Xdock:icon=$APP_HOME/media/gradle.icns""
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin ; then
    APP_HOME=`cygpath --path --windows "$APP_HOME"`
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    WRAPPER_JAR=`cygpath --path --windows "$WRAPPER_JAR"`
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
    JAVACMD_LAUNCHER=`cygpath --path --windows "$JAVACMD_LAUNCHER"`
fi

# Escape application args
save () {
    for i do printf %s\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/'/" ; done
    echo " "
}
APP_ARGS_ESCAPED=`save "$@"`

# Collect all arguments for the java command, following the shell quoting and substitution rules
eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS ""-Dorg.gradle.appname=$APP_BASE_NAME"" -classpath ""$WRAPPER_JAR"" org.gradle.wrapper.GradleWrapperMain "$@"

# Start the wrapper
#
# Before executing, echo the command line that is going to be executed if
# DEBUG_GRADLE_LAUNCHER is set. This is useful for debugging the launcher script.
# Set DEBUG_GRADLE_LAUNCHER=true to see the command line.
if [ "$DEBUG_GRADLE_LAUNCHER" = "true" ]; then
    echo "starting gradle wrapper with command:"
    printf "%s
" "$JAVACMD_LAUNCHER $@"
fi
"$JAVACMD_LAUNCHER" "$@"
