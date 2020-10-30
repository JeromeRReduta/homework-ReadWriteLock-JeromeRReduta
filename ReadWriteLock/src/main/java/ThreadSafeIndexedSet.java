import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A thread-safe version of {@link IndexedSet} using a read/write lock.
 *
 * @param <E> element type
 * @see IndexedSet
 * @see SimpleReadWriteLock
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
public class ThreadSafeIndexedSet<E> extends IndexedSet<E> {

	/** The lock used to protect concurrent access to the underlying set. */
	private final SimpleReadWriteLock lock;

	/**
	 * Initializes an unsorted thread-safe indexed set.
	 */
	public ThreadSafeIndexedSet() {
		// NOTE: DO NOT MODIFY THIS METHOD
		this(false);
	}

	/**
	 * Initializes a thread-safe indexed set.
	 *
	 * @param sorted whether the set should be sorted
	 */
	public ThreadSafeIndexedSet(boolean sorted) {
		// NOTE: DO NOT MODIFY THIS METHOD
		super(sorted);
		lock = new SimpleReadWriteLock();
	}

	/**
	 * Returns the identity hashcode of the lock object. Not particularly useful.
	 *
	 * @return the identity hashcode of the lock object
	 */
	public int lockCode() {
		// NOTE: DO NOT MODIFY THIS METHOD
		return System.identityHashCode(lock);
	}

	
	public <I, O> O readOp(Function<I, O> methodRef, I input) {
	
		lock.readLock().lock();
		
		try {
			return methodRef.apply(input);
		}
		finally {
			lock.readLock().unlock();
		}
		
	}
	
	public<O> O readOp(Supplier<O> methodRef) {
		
		lock.readLock().lock();
		
		try {
			return methodRef.get();
		}
		finally {
			lock.readLock().unlock();
		}
		
	}
	
	public <I, O> O writeOp(Function<I, O> methodRef, I input) {
		
		lock.writeLock().lock();
		
		try {
			return methodRef.apply(input);
		}
		finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean add(E element) {
		return writeOp(super::add, element);
	}
	
	@Override
	public boolean addAll(Collection<E> elements) {
		return writeOp(super::addAll, elements);
	}
	
	@Override
	public boolean addAll(IndexedSet<E> elements) {
		return writeOp(super::addAll, elements);
	}
	
	@Override
	public int size() {
		return readOp(super::size);
	}
	
	@Override
	public E get(int index) {
		return readOp(super::get, index);
	}
	
	@Override
	public IndexedSet<E> unsortedCopy() {
		
		return readOp(super::unsortedCopy);
		
	}
	
	@Override
	public IndexedSet<E> sortedCopy() {
		
		return readOp(super::sortedCopy);
		
	}
	
	@Override
	public String toString() {
		
		return readOp(super::toString);
		
	}
	
}

