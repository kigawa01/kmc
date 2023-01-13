package net.kigawa.kmcmanager.event

interface CancelableEvent: Event {
    var cancel: Boolean
}