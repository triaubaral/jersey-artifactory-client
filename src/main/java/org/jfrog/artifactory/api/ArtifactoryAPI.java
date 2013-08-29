package org.jfrog.artifactory.api;

public interface ArtifactoryAPI<T> {
	T exportTo(final T pFile);
	void importFrom(final T pFile);
	void delete();
}
