package control;

import java.util.Objects;
/**
 * this class is a generalisation of the Java class BiConsumer but with 3 arguments
 * 
 * @author PhysicDev
 * 
 * @version 1.0
 *
 * @param <T> first arg of the method
 * @param <U> second arg of the method
 * @param <V> third arg of the method
 */
public interface TriConsumer<T,U,V> {
	//copier coller de l'interface BiConsumer mais avec trois argument, je suis pas trop sur de comment ça marche mais ça marche (normalement)
	
	/**
	 * this method call the method attached to the class instance
	 * @param t arg1
	 * @param u arg2
	 * @param v arg3
	 */
	void accept(T t, U u,V v);
	default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
	       Objects.requireNonNull(after);
	       return (l, r, v) -> {
	           accept(l, r, v);
	           after.accept(l, r, v);
	       };
	}
}
