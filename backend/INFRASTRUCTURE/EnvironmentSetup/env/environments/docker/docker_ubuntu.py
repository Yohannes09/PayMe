
from ...command_runner.CommandRunner import CommandRunner

runner = CommandRunner()

def setup_apt_repository():
    commands = {
        "Installing required packages ": "apt-get install ca-certificates curl",
        "Creating keyrings directory ": "install -m 0755 -d /etc/apt/keyrings",
        "Downloading Docker GPG key ": "curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc",
        "Setting permissions on Docker GPG key ": "chmod a+r /etc/apt/keyrings/docker.asc"
    }

    for description, command in commands.items():
        runner.run_sudo_command(command, description)

    runner.update()

    command = '''echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
      $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null'''

    runner.run_command(command, "Adding repository to Apt sources. ")

    runner.update()

def install_docker():
    runner.run_command("sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin", "Installing Docker. ")

def run():
    print("Beginning Docker setup and install. ")
    setup_apt_repository()
    print("Setup complete. Installing Docker...")
    install_docker()
    print("Docker installed! ")

if __name__ == "__main__":
    run()