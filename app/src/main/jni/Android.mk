LOCAL_PATH := $(call my-dir)

#bitmap operations module
include $(CLEAR_VARS)

LOCAL_MODULE    := JniBitmapStorageTest
LOCAL_SRC_FILES := JniBitmapStorageTest.cpp
LOCAL_LDLIBS := -llog
LOCAL_LDFLAGS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
APP_OPTIM := debug
LOCAL_CFLAGS := -g