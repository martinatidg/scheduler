package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestZipDirectory implements TestZip {
	private Node<String> root;
	private Path zipPath;

	public TestZipDirectory(String testDir, String zipDir) throws IOException {
		root = new Node<String>(Paths.get(testDir), true);
		zipPath = Paths.get(zipDir);
		setupDirTree();
	}

	public void initialize() throws IOException {
		createFileTree(root);
	}

	public void clean() throws IOException {
		deleteFileTree(root);
		Files.deleteIfExists(zipPath);
	}

	private void setupDirTree() throws IOException {
		List<Node<String>> children = new ArrayList<>();

		Path path = root.getPath().resolve("dir1");
		Node<String> node = CreateDirWithChildren(path.toString(), "data1", 2, 3);
		children.add(node);

		path = root.getPath().resolve("dir2");
		node = CreateDirWithChildren(path.toString(), "data2", 3, 2);
		children.add(node);

		path = root.getPath().resolve("dir3");
		node = CreateDirWithChildren(path.toString(), "data3", 4, 1);
		children.add(node);

		root.setChildren(children);
	}

	private void createFile(Path path, String content) throws IOException {
		Files.createFile(path);
		Files.write(path, content.getBytes());
	}

	private void createFile(Node<String> node) throws IOException {
		createFile(node.getPath(), node.getData());
	}

	private List<Node<String>> createDataNodes(String pathName, String data, int nodeNum) {
		List<Node<String>> nodeList = new ArrayList<>();
		for (int i = 1; i <= nodeNum; i++) {
			String fname =Paths.get(pathName).getFileName().toString();
			Path filePath = Paths.get(pathName, fname + "_" + i + ".txt");
			Node<String> node = new Node<>(filePath, data + "_" + i);
			nodeList.add(node);
		}

		return nodeList;
	}

	private List<Node<String>> createDirNodes(String pathName, int nodeNum) {
		List<Node<String>> nodeList = new ArrayList<>();
		for (int i = 1; i <= nodeNum; i++) {
			String fname = Paths.get(pathName).getFileName().toString();
			Path dirPath = Paths.get(pathName, fname + "_" + i);
			Node<String> node = new Node<>(dirPath, true);
			nodeList.add(node);
		}

		return nodeList;
	}

	private Node<String> CreateDirWithChildren(String dir, String data, int dirNum, int dataNum) {
		List<Node<String>> dataNodes = createDataNodes(dir, data, dataNum);
		List<Node<String>> dirNodes = createDirNodes(dir, dirNum);

		dataNodes.addAll(dirNodes); // make the directory have mixed data file and directories
		Path nodePath = Paths.get(dir);
		Node<String> node = new Node<>(nodePath, dataNodes, true);

		return node;
	}

	private void createFileTree(Node<String> topRoot) throws IOException {
		if (topRoot == null) {
			return;
		}

		for (Node<String> node : topRoot.getChildren()) {
			if (node.isDirectory()) {
				Files.createDirectories(node.getPath());
				if (node.getChildren() != null) {
					createFileTree(node);
				}
			} else {
				createFile(node);
			}
		}
	}

	private void deleteFileTree(Node<String> topRoot) throws IOException {
		if (topRoot == null) {
			return;
		}

		for (Node<String> node : topRoot.getChildren()) {
			if (node.isDirectory()) {
				Files.createDirectories(node.getPath());
				if (node.getChildren() != null) {
					deleteFileTree(node);
				}
			}
			Files.deleteIfExists(node.getPath());
		}

		Files.deleteIfExists(topRoot.getPath());
	}
}

class Node<T> {
	private Path path = null;
	private T data = null;
	private List<Node<T>> children = null;
	private boolean isDirectory = false;

	public Node(Path path, List<Node<T>> children, boolean isDirectory) {
		this.path = path;
		this.children = children;
		this.isDirectory = isDirectory;
	}

	// construct an empty directory node
	public Node(Path path, boolean isDirectory) {
		this.path = path;
		this.isDirectory = isDirectory;
	}

	// construct a file node
	public Node(Path path, T data) {
		this.path = path;
		this.data = data;
		this.isDirectory = false;
	}

	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDir) {
		this.isDirectory = isDir;
	}

	@Override
	public String toString() {
		return "Node [path=" + path + ", data=" + data + ", children=" + children + ", isDirectory=" + isDirectory
				+ "]";
	}
}
