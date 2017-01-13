package vntu.academic.publications.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutionHelper {
	public static void executeAllAwaitCompletion(List<? extends Runnable> tasks)
			throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
		List<Future<?>> futures = new ArrayList<>();
		for (Runnable task : tasks) {
			futures.add(executor.submit(task));
		}
		for (Future<?> future : futures)
			future.get();
		executor.shutdown();
	}
}
