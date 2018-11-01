package ch.wengle.demoapp.api.msg;

@FunctionalInterface
public interface HeaderBuilderProvider {
	public void provide(HeaderBuilder headerBuilder);
}