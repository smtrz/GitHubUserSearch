package com.tahir.anylinetask.viewstate

/**
 * Sealed Class with static instance that will be used for setting up statuses.
 *
 */

sealed class SubmitStatus(
) {

    object getData : SubmitStatus()
    object None : SubmitStatus()
    object Book : SubmitStatus()
    object Search : SubmitStatus()

}