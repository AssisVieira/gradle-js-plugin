/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eriwen.gradle.js

import com.eriwen.gradle.js.source.JavaScriptSourceSetContainer
import com.eriwen.gradle.js.source.internal.DefaultJavaScriptSourceSetContainer
import com.eriwen.gradle.js.source.internal.InternalGradle
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil
import org.gradle.api.internal.artifacts.ResolverFactory;
import org.gradle.api.internal.artifacts.repositories.layout.PatternRepositoryLayout;

class JavaScriptExtension {
    public static final NAME = "javascript"

    public static final String GRADLE_PUBLIC_JAVASCRIPT_REPO_URL = "http://repo.gradle.org/gradle/javascript-public";
    public static final String GOOGLE_APIS_REPO_URL = "http://ajax.googleapis.com/ajax/libs";

    final JavaScriptSourceSetContainer source
    private final ResolverFactory resolverFactory;

    public JavaScriptExtension(Project project /*, ResolverFactory resolverFactoryIn */) {
        source = InternalGradle.toInstantiator(project).newInstance(DefaultJavaScriptSourceSetContainer, project)
//        this.resolverFactory = resolverFactoryIn;
    }

    void source(Closure closure) {
        ConfigureUtil.configure(closure, source)
    }

    public ArtifactRepository getGradlePublicJavaScriptRepository() {
        MavenArtifactRepository repo = resolverFactory.createMavenRepository();
        repo.setUrl(GRADLE_PUBLIC_JAVASCRIPT_REPO_URL);
        repo.setName("Gradle Public JavaScript Repository");
        return repo;
    }

    public ArtifactRepository getGoogleApisRepository() {
        IvyArtifactRepository repo = resolverFactory.createIvyRepository();
        repo.setName("Google Libraries Repository");
        repo.setUrl(GOOGLE_APIS_REPO_URL);
        repo.layout("pattern", new Closure(this) {
            public void doCall() {
                PatternRepositoryLayout layout = (PatternRepositoryLayout) getDelegate();
                layout.artifact("[organization]/[revision]/[module].[ext]");
                layout.ivy("[organization]/[revision]/[module].xml");
            }
        });
        return repo;
    }
}
