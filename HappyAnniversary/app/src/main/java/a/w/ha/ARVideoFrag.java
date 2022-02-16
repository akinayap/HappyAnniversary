package a.w.ha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.io.IOException;
import java.io.InputStream;

public class ARVideoFrag extends ArFragment {
	MediaPlayer mediaPlayer;
	ExternalTexture externalTexture;
	ModelRenderable videoRenderable;
	VideoAnchorNode videoAnchorNode;

	AugmentedImage activeAugmentedImage = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaPlayer = new MediaPlayer();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = super.onCreateView(inflater, container, savedInstanceState);

		getArSceneView().setVisibility(View.GONE);

		getPlaneDiscoveryController().hide();
		getPlaneDiscoveryController().setInstructionView(null);
		getArSceneView().getPlaneRenderer().setEnabled(false);
		getArSceneView().setLightEstimationEnabled(false);

		initializeSession();
		createArScene();

		return view;
	}

	@Override
	protected Config getSessionConfiguration(Session session) {
		Config c = super.getSessionConfiguration(session);
		c.setLightEstimationMode(Config.LightEstimationMode.DISABLED);
		c.setFocusMode(Config.FocusMode.AUTO);
		if(!setupAugmentedImageDatabase(c, session))
		{
			Toast.makeText(requireContext(), "Could not setup augmented image database", Toast.LENGTH_LONG).show();
		}
		return c;
	}

	private boolean setupAugmentedImageDatabase(Config c, Session session) {
		try {
			AugmentedImageDatabase db = new AugmentedImageDatabase(session);
			db.addImage(FILES.GOLF_VIDEO, loadAugmentedImageBitmap(FILES.GOLF_IMAGE));
			//db.addImage(FILES.ALICE_VIDEO, loadAugmentedImageBitmap(FILES.ALICE_IMAGE));
			//db.addImage(FILES.ARCHERY_VIDEO, loadAugmentedImageBitmap(FILES.ARCHERY_IMAGE));
			//db.addImage(FILES.CIRCUS_VIDEO, loadAugmentedImageBitmap(FILES.CIRCUS_IMAGE));
			//db.addImage(FILES.FDTT_VIDEO, loadAugmentedImageBitmap(FILES.FDTT_IMAGE));
			//db.addImage(FILES.MW_VIDEO, loadAugmentedImageBitmap(FILES.MW_IMAGE));
			//db.addImage(FILES.NM_VIDEO, loadAugmentedImageBitmap(FILES.NM_IMAGE));
			db.addImage(FILES.PARIS_VIDEO, loadAugmentedImageBitmap(FILES.PARIS_IMAGE));
			db.addImage(FILES.TAKO_VIDEO, loadAugmentedImageBitmap(FILES.TAKO_IMAGE));
			//db.addImage(FILES.VENDING_VIDEO, loadAugmentedImageBitmap(FILES.VENDING_IMAGE));
			c.setAugmentedImageDatabase(new AugmentedImageDatabase(session));
			return true;
		} catch (Exception e){
			Log.e("Error", "Could not add bitmap to augmented image database");
		}
		return false;
	}

	private Bitmap loadAugmentedImageBitmap(String img) {
		InputStream bm = null;
		try {
			bm = requireContext().getAssets().open(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(bm);
	}

	private void createArScene() {
		externalTexture = new ExternalTexture();
		mediaPlayer.setSurface(externalTexture.getSurface());

		ModelRenderable.builder()
						.setSource(requireContext(), R.raw.augmented_video_model)
						.build()
						.thenAccept(r-> {
							videoRenderable = r;
							r.setShadowCaster(false);
							r.setShadowReceiver(false);
							r.getMaterial().setExternalTexture("videoTexture", externalTexture);
						})
		.exceptionally(throwable -> {
			Log.e("Error", "Could not create ModelRenderable", throwable);
			return null;
		});

		videoAnchorNode = new VideoAnchorNode();
		videoAnchorNode.setParent(getArSceneView().getScene());
	}
}
