package com.example.trail.reactive;

import io.reactivex.rxjava3.core.Scheduler;

public interface RxScheduler {

    Scheduler io();

    Scheduler ui();
}
