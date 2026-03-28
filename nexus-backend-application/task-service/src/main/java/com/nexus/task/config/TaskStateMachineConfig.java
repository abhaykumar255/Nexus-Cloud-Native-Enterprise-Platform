package com.nexus.task.config;

import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.domain.enums.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

/**
 * Spring State Machine configuration for Task lifecycle
 */
@Slf4j
@Configuration
@EnableStateMachineFactory
public class TaskStateMachineConfig extends StateMachineConfigurerAdapter<TaskStatus, TaskEvent> {
    
    @Override
    public void configure(StateMachineStateConfigurer<TaskStatus, TaskEvent> states) throws Exception {
        states
                .withStates()
                .initial(TaskStatus.TODO)
                .states(EnumSet.allOf(TaskStatus.class))
                .end(TaskStatus.COMPLETED)
                .end(TaskStatus.CANCELLED);
    }
    
    @Override
    public void configure(StateMachineTransitionConfigurer<TaskStatus, TaskEvent> transitions) throws Exception {
        transitions
                // TODO transitions
                .withExternal()
                    .source(TaskStatus.TODO).target(TaskStatus.IN_PROGRESS)
                    .event(TaskEvent.START)
                    .and()
                .withExternal()
                    .source(TaskStatus.TODO).target(TaskStatus.CANCELLED)
                    .event(TaskEvent.CANCEL)
                    .and()
                
                // IN_PROGRESS transitions
                .withExternal()
                    .source(TaskStatus.IN_PROGRESS).target(TaskStatus.IN_REVIEW)
                    .event(TaskEvent.SUBMIT_REVIEW)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_PROGRESS).target(TaskStatus.COMPLETED)
                    .event(TaskEvent.COMPLETE)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_PROGRESS).target(TaskStatus.BLOCKED)
                    .event(TaskEvent.BLOCK)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_PROGRESS).target(TaskStatus.ON_HOLD)
                    .event(TaskEvent.HOLD)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_PROGRESS).target(TaskStatus.CANCELLED)
                    .event(TaskEvent.CANCEL)
                    .and()
                
                // IN_REVIEW transitions
                .withExternal()
                    .source(TaskStatus.IN_REVIEW).target(TaskStatus.COMPLETED)
                    .event(TaskEvent.APPROVE)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_REVIEW).target(TaskStatus.IN_PROGRESS)
                    .event(TaskEvent.REQUEST_CHANGES)
                    .and()
                .withExternal()
                    .source(TaskStatus.IN_REVIEW).target(TaskStatus.CANCELLED)
                    .event(TaskEvent.CANCEL)
                    .and()
                
                // BLOCKED transitions
                .withExternal()
                    .source(TaskStatus.BLOCKED).target(TaskStatus.IN_PROGRESS)
                    .event(TaskEvent.UNBLOCK)
                    .and()
                .withExternal()
                    .source(TaskStatus.BLOCKED).target(TaskStatus.CANCELLED)
                    .event(TaskEvent.CANCEL)
                    .and()
                
                // ON_HOLD transitions
                .withExternal()
                    .source(TaskStatus.ON_HOLD).target(TaskStatus.IN_PROGRESS)
                    .event(TaskEvent.RESUME)
                    .and()
                .withExternal()
                    .source(TaskStatus.ON_HOLD).target(TaskStatus.CANCELLED)
                    .event(TaskEvent.CANCEL);
    }
}

