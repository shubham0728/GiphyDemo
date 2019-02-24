package com.ss.giphy;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ss.giphy.model.GiphyEntity;
import com.ss.giphy.repository.GiphyRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.ss.giphy", appContext.getPackageName());
	}

	private GiphyRepository repo;

	@Test
	public void insertTest() throws InterruptedException {
		Context appContext = InstrumentationRegistry.getTargetContext();
		repo = new GiphyRepository(appContext);
		String id = "1234567890";
		String type = "gif";
		String url = "https://media0.giphy.com/media/BpS84BEu5aGcV4ZqHn/giphy.gif";

		GiphyEntity entity = new GiphyEntity();
		entity.setId(id);
		entity.setType(type);
		entity.setUrl(url);
		entity.setFav(false);

		repo.insert(entity);
		Thread.sleep(2500);
	}
}
