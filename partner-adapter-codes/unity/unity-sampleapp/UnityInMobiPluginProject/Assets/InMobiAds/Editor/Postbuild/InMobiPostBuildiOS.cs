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


		private static string nativeCodeInUnityPath = "Assets/Plugins/iOS";
		private static string nativeCodeInXcodePath = "InMobi";

		[PostProcessBuild (100)]
		public static void OnPostprocessBuild (BuildTarget buildTarget, string buildPath)
		{
			// BuiltTarget.iOS is not defined in Unity 4, so we just use strings here
			if (buildTarget.ToString () == "iOS" || buildTarget.ToString () == "iPhone") {
				PrepareProject (buildPath);
				InjectNativeCode (buildPath);
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
			

		private static void InjectNativeCode (string buildPath)
		{
			string projPath = Path.Combine (buildPath, "Unity-iPhone.xcodeproj/project.pbxproj");
			string nativeCodeInUnityFullPath = Path.Combine (Directory.GetCurrentDirectory (), nativeCodeInUnityPath);
			string nativeCodeInXcodeFullPath = Path.Combine (buildPath, nativeCodeInXcodePath);

			PBXProject project = new PBXProject ();
			project.ReadFromString (File.ReadAllText (projPath));

			DirectoryCopy (nativeCodeInUnityFullPath, nativeCodeInXcodeFullPath, true);

			string targetGuid = project.TargetGuidByName ("Unity-iPhone");
			string[] files = Directory.GetFiles (nativeCodeInXcodeFullPath, "*", SearchOption.AllDirectories);

			foreach (string fileFullPath in files) {
				string fileExt = Path.GetExtension (fileFullPath);
				if (fileExt.Equals(".meta") || fileExt.Equals(".txt")) {
					continue;
				}

				string fileName = Path.GetFileName (fileFullPath);
				string fileGuid = project.AddFile (fileFullPath, Path.Combine (nativeCodeInXcodePath, fileName));
				project.AddFileToBuild (targetGuid, fileGuid);
			}

			string[] subdirs = Directory.GetDirectories (nativeCodeInXcodeFullPath);
			foreach (string subdir in subdirs) {
				project.AddBuildProperty (targetGuid, "HEADER_SEARCH_PATHS", subdir);

				AddAllFoundFrameworks (project, targetGuid, subdir);
			}
			File.WriteAllText (projPath, project.WriteToString ());
		}

		private static void AddAllFoundFrameworks (PBXProject project, string targetGuid, string source)
		{
			string[] dirs = Directory.GetDirectories (source);
			foreach (string dir in dirs) {
				if (dir.EndsWith (".framework")) {
					string fileGuid = project.AddFile (dir, "Frameworks/" + dir.Substring (dir.LastIndexOf ("/") + 1));
					project.AddFileToBuild (targetGuid, fileGuid);
					project.AddBuildProperty (targetGuid, "FRAMEWORK_SEARCH_PATHS", source);
				}
				AddAllFoundFrameworks (project, targetGuid, dir);
			}
		}
	}
}
