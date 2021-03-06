package com.vortexwolf.dvach.test;

import com.vortexwolf.dvach.R;
import com.vortexwolf.dvach.models.domain.IAttachmentEntity;
import com.vortexwolf.dvach.models.presentation.AttachmentInfo;
import com.vortexwolf.dvach.services.presentation.DvachUriBuilder;
import com.vortexwolf.dvach.settings.ApplicationSettings;
import com.vortexwolf.dvach.test.mocks.MockAttachmentEntity;

import android.app.Instrumentation;
import android.net.Uri;
import android.test.InstrumentationTestCase;

public class AttachmentInfoTest extends InstrumentationTestCase  {
	
	private final String mBoardCode = "test";
	private ApplicationSettings mSettings;
	private DvachUriBuilder mDvachUriBuilder;

	protected void setUp() throws Exception {
		Instrumentation instr = this.getInstrumentation();
		mSettings = new ApplicationSettings(instr.getContext(), instr.getContext().getResources(), null);
		mDvachUriBuilder = new DvachUriBuilder(Uri.parse("http://2ch.hk"));
	}
	
	public void testEmptyAttachment(){
		IAttachmentEntity entity = new MockAttachmentEntity("", "", 0, "");
		AttachmentInfo info = new AttachmentInfo(entity, this.mBoardCode, this.mDvachUriBuilder);
		
		assertTrue(info.isEmpty());
		assertEquals(info.getDescription(""), "");
		assertEquals(info.getSourceUrl(mSettings), null);
		assertEquals(info.getThumbnailUrl(), null);
		assertEquals(info.getSourceExtension(), null);
		assertEquals(info.getDefaultThumbnail(), R.drawable.page_white_4x);
	}
	
	public void testVideoAttachment(){
		String videoHtml = "<object width=\"320\" height=\"262\"><param name=\"movie\" value=\"http://www.youtube.com/v/dQw4w9WgXcQ\"></param><param name=\"wmode\" value=\"transparent\"></param><embed src=\"http://www.youtube.com/v/dQw4w9WgXcQ\" type=\"application/x-shockwave-flash\" wmode=\"transparent\" width=\"320\" height=\"262\"></embed></object>";
		IAttachmentEntity entity = new MockAttachmentEntity(null, null, 0, videoHtml);
		AttachmentInfo info = new AttachmentInfo(entity, this.mBoardCode, this.mDvachUriBuilder);
		
		assertFalse(info.isEmpty());
		assertEquals(info.getDescription(""), "YouTube");
		assertEquals(info.getSourceUrl(mSettings), "http://www.youtube.com/v/dQw4w9WgXcQ");
		assertEquals(info.getThumbnailUrl(), "http://img.youtube.com/vi/dQw4w9WgXcQ/default.jpg");
		//Проверю и те поля, которые не должны вызываться
		assertEquals(info.getSourceExtension(), null);
		assertEquals(info.getDefaultThumbnail(), R.drawable.page_white_4x);
	}
	
	public void testNonImageAttachment(){
		IAttachmentEntity entity = new MockAttachmentEntity("src/123.mp3", null, 9000, "");
		AttachmentInfo info = new AttachmentInfo(entity, this.mBoardCode, this.mDvachUriBuilder);
		
		assertFalse(info.isEmpty());
		assertEquals(info.getDescription("Kb"), "9000Kb");
		assertEquals(info.getSourceUrl(mSettings), "http://2ch.hk/test/src/123.mp3");
		assertEquals(info.getThumbnailUrl(), null);
		assertEquals(info.getSourceExtension(), "mp3");
		assertEquals(info.getDefaultThumbnail(), R.drawable.page_white_sound_4x);
	}
	
	public void testImageAttachment(){
		IAttachmentEntity entity = new MockAttachmentEntity("src/123.jpg", "thumb/123s.jpg", 9000, "");
		AttachmentInfo info = new AttachmentInfo(entity, this.mBoardCode, this.mDvachUriBuilder);
		
		assertFalse(info.isEmpty());
		assertEquals(info.getDescription("Kb"), "9000Kb");
		assertEquals(info.getSourceUrl(mSettings), "http://2ch.hk/test/src/123.jpg");
		assertEquals(info.getThumbnailUrl(), "http://2ch.hk/test/thumb/123s.jpg");
		assertEquals(info.getSourceExtension(), "jpg");
		assertEquals(info.getDefaultThumbnail(), R.drawable.page_white_4x);
	}
}
