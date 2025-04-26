import subprocess

class CommandRunner:

    def command(self, command, description):
        print(f"---{description}---")
        print(f"$ {command}")

        result = subprocess.run(command, shell=True)

        if result.returncode != 0:
            print(f"Command failed: {command}")
            exit(1)

        print("Done.\n")

    def update(self):
        self.command("sudo apt-get update -y", "Updating packages lists. ")

    def upgrade(self):
        self.command("sudo apt-get upgrade -y", "Upgrading installed packages. ")

    def update_and_upgrade(self):
        self.command("sudo apt-get update && sudo apt-get upgrade -y", "Updating and upgrading packages. ")

    def run_command(self, command, description):
        self.command(command, description)

    def run_sudo_command(self, command, description):
        sudo_command = f"sudo {command}"
        self.command(sudo_command, description)