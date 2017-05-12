namespace InMobiAds.Editor.Postbuild
{
	using UnityEngine;
	using System.Collections;
	using System.Collections.Generic;
	using System.IO;
	using InMobiAds.Editor.ThirdParty.xcodeapi;

	public class PostBuildiOS
	{
		protected static readonly string inMobiPluginsPath = "Plugins/iOS/InMobi";

		public static string NormalizePathForPlatform(string path)
		{
			return path.Replace ('/', Path.DirectorySeparatorChar);
		}

		public static void DirectoryCopy(string sourceDirName, string destDirName, bool copySubDirs)
		{
			// Get the subdirectories for the specified directory.
			DirectoryInfo dir = new DirectoryInfo(sourceDirName);
			if (!dir.Exists) {
				throw new DirectoryNotFoundException(
					"Source directory does not exist or could not be found: "
					+ sourceDirName);
			}

			// If the destination directory doesn't exist, create it.
			if (!Directory.Exists(destDirName)) {
				Directory.CreateDirectory(destDirName);
			}

			// Get the files in the directory and copy them to the new location.
			FileInfo[] files = dir.GetFiles();
			foreach (FileInfo file in files) {
				string temppath = NormalizePathForPlatform (Path.Combine(destDirName, file.Name));
				file.CopyTo(temppath, false);
			}

			// If copying subdirectories, copy them and their contents to new location.
			if (copySubDirs) {
				DirectoryInfo[] dirs = dir.GetDirectories();
				foreach (DirectoryInfo subdir in dirs) {
					string temppath = NormalizePathForPlatform (Path.Combine(destDirName, subdir.Name));
					DirectoryCopy(subdir.FullName, temppath, copySubDirs);
				}
			}
		}

		protected static void AddPlatformFrameworksToProject(string[] frameworks, PBXProject project, string target)
		{
			foreach (string framework in frameworks) {
				if (!project.HasFramework (framework)) {
					Debug.Log ("Adding " + framework + " to Xcode project");
					project.AddFrameworkToProject (target, framework, false);
				}
			}
		}
		
		protected static void AddPlatformLibsToProject(string[] libs, PBXProject project, string target)
		{
			foreach (string lib in libs) {
				string libGUID = project.AddFile ("usr/lib/" + lib, "Libraries/" + lib, PBXSourceTree.Sdk);
				project.AddFileToBuild (target, libGUID);
			}	
		}

		protected static void AddBuildProperty(PBXProject project, string target, string property, string value)
		{
			project.AddBuildProperty (target, property, value);
		}
	}

}
