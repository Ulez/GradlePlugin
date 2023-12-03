package com.lcy.plugin;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        System.out.println("afterEvaluate execute 000000" );
        final MyExtension x = target.getExtensions().create("MyExtension", MyExtension.class);
        target.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                System.out.println("afterEvaluate execute11111" + x.getName());
            }
        });
    }
}