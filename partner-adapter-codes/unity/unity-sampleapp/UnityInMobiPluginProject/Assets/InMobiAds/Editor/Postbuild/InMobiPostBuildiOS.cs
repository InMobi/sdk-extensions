namespace InMobiAds.Editor.Postbuild
{
	using System;
	using UnityEditor;
	using UnityEngine;
	using UnityEditor.Callbacks;
	using System.IO;
	using System.Collections.Generic;
	using System.Linq;
	using InMobiAds.Editor.Postbuild;
	using InMobiAds.Editor.ThirdParty.xcodeapi;

	public class InMobiPostBuildiOS : PostBuildiOS
	{
		private static string[] platformFrameworks = new string[] {
			"WebKit.framework"
		};

		private static string[] platformLibs = new string[] {
			"libz.dylib",
			"libsqlite3.0.dylib"
		};

		[PostProcessBuild (100)]
		public static void OnPostprocessBuild (BuildTarget buildTarget, string buildPath)
		{
			// BuiltTarget.iOS is not defined in Unity 4, so we just use strings here
			if (buildTarget.ToString () == "iOS" || buildTarget.ToString () == "iPhone") {
				PrepareProject (buildPath);
			}
		}
	

		private static void PrepareProject (string buildPath)
		{
			string projPath = Path.Combine (buildPath, "Unity-iPhone.xcodeproj/project.pbxproj");
			PBXProject project = new PBXProject ();
			project.ReadFromString (File.ReadAllText (projPath));
			string target = project.TargetGuidByName ("Unity-iPhone");

			AddPlatformFrameworksToProject (platformFrameworks, project, target);
			AddPlatformLibsToProject (platformLibs, project, target);
			AddBuildProperty (project, target, "OTHER_LDFLAGS", "-ObjC");
			AddBuildProperty (project, target, "CLANG_ENABLE_MODULES", "YES");

			File.WriteAllText (projPath, project.WriteToString ());
		}
	
	}
}
