/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */

package ti.camerax

import org.appcelerator.kroll.KrollModule
import org.appcelerator.kroll.annotations.Kroll
import org.appcelerator.kroll.common.Log
import org.appcelerator.kroll.common.TiConfig
import org.appcelerator.titanium.TiApplication

@Kroll.module(name = "TiCamerax", id = "ti.camerax")
class TiCameraxModule : KrollModule() {

    companion object {
        // Standard Debugging variables
        private const val LCAT = "TiCameraxModule"
        private val DBG = TiConfig.LOGD

        @Kroll.constant
        val FLASH_MODE_AUTO = 0
        @Kroll.constant
        val FLASH_MODE_ON = 1
        @Kroll.constant
        val FLASH_MODE_OFF = 2

        @Kroll.constant
        val TORCH_MODE_AUTO = 0
        @Kroll.constant
        val TORCH_MODE_ON = 1
        @Kroll.constant
        val TORCH_MODE_OFF = 2

        @Kroll.constant
        val DEVICE_POSITION_UNSPECIFIED = -1
        @Kroll.constant
        val DEVICE_POSITION_BACK = -1
        @Kroll.constant
        val DEVICE_POSITION_FRONT = -1
        @Kroll.constant
        val DEVICE_TYPE_MICROPHONE = -1
        @Kroll.constant
        val DEVICE_TYPE_DUO_CAMERA = -1
        @Kroll.constant
        val DEVICE_TYPE_WIDE_ANGLE_CAMERA = -1
        @Kroll.constant
        val DEVICE_TYPE_TELE_PHOTO_CAMERA = -1
        @Kroll.constant
        val DEVICE_TYPE_ULTRA_WIDE_ANGLE_CAMERA = -1
        @Kroll.constant
        val DEVICE_TYPE_TRIPLE_CAMERA = -1

        @Kroll.constant
        val FOCUS_MODE_AUTO_FOCUS = 0
        @Kroll.constant
        val FOCUS_MODE_CONTINUOUS_AUTO_FOCUS = 1
        @Kroll.constant
        val FOCUS_MODE_LOCKED = 2

        @Kroll.onAppCreate
        fun onAppCreate(app: TiApplication?) {
            Log.d(LCAT, "inside onAppCreate")
            // put module init code that needs to run when the application is created
        }
    }
}
